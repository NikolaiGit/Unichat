package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;

import message.Message;
import message.MessageCreateChat;
import message.MessageDeregisterClient;
import message.MessageEnterChat;
import message.MessageFactory;
import message.MessageLeaveChat;
import message.MessageRegisterClient;
import message.MessageRequestChats;
import message.MessageSendMsgInChat;
import message.MessageType;

import message.Util;
import message.dataChat;
import message.dataClient;

public class Server implements Runnable {
	/**
	 * 
	 * @author Nikolai Seip
	 *
	 */

	private final int serverPort;
	private boolean running = true;
	ServerSocket serverSo;
	MulticastSocket multiSo;
	MCastAdrPool pool;
	HashMap<Integer, dataChat> chats;
	HashMap<Integer, dataClient> clients;


	public Server(int serverPort) {
		this.serverPort = serverPort;
		pool = new MCastAdrPool();
		chats = new HashMap<>();
		clients = new HashMap<>();
	}


	public int getServerPort() {
		return serverPort;
	}


	public void stopServer() {
		running = false;
		try {
			serverSo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void run() {
		try {
			serverSo = new ServerSocket(serverPort);
			multiSo = new MulticastSocket(serverPort);
			multiSo.setTimeToLive(1);

			Socket clientSo = null;
			while (running) {
				clientSo = serverSo.accept();
				Thread t = new Thread(new MessageHandler(clientSo, multiSo));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IOFehler beim Erzeugen des ServerSockets oder des MulticastSockets");
		} finally {
			if (serverSo != null) {
				try {
					serverSo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (multiSo != null) {
				multiSo.close();
			}
		}

	}

	private class MessageHandler implements Runnable {
		private Socket s;


		public MessageHandler(Socket client, MulticastSocket multiSo) {
			this.s = client;
		}


		@Override
		public void run() {
			try (Socket client = s;
					ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {

				Message m = (Message) in.readObject();
				Message response = null;

				switch (m.getType()) {
				case createChat:
					response = createChat(m);
					break;
				case deregisterClient:
					response = deregisterClient(m);
					break;
				case enterChat:
					response = enterChat(m);
					break;
				case requestChats:
					response = requestChats(m);
					break;
				case leaveChat:
					response = leaveChat(m);
					break;
				case registerClient:
					response = registerClient(m, client.getRemoteSocketAddress());
					break;
				case sendMsgInChat:
					response = sendMsgInChat(m);
					break;
				default:
					throw new RuntimeException("Invalid message type");
				}
				out.writeObject(response);
				out.flush();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Kann kein Objekt aus dem Stream lesen ... Klasse nicht auffindbar");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		private Message registerClient(Message m, SocketAddress clientAddresse) {
			if (m.getType() != MessageType.registerClient) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageRegisterClient mrc = (MessageRegisterClient) m;
			// Erzeuge ID für Client
			// Bereich 0-999
			Integer id = null;
			while (true) {
				id = new Integer((int) (Math.random() * 1000));
				if (!clients.containsKey(id))
					break;
			}
			dataClient dc = new dataClient(id, mrc.getName(), clientAddresse);
			clients.put(dc.getID(), dc);
			System.out.println("Ein Client mit dem Namen " + dc.getName() + " hat sich unter der ID " + dc.getID()
					+ " registriert");
			return MessageFactory.createRegisterClientMsg(mrc.getName(), id, true);

		}


		private Message createChat(Message m) {
			if (m.getType() != MessageType.createChat) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageCreateChat mcc = (MessageCreateChat) m;
			// ermittle Multicastadresse des Chats
			if (pool.isEmpty()) {
				return MessageFactory.createCreateChatMsg(null, null, 0, false);
			}
			InetAddress mcastadr = pool.getMCastAdr();

			// Erzeuge ID für Chat
			// bereich 1000-1999
			Integer id = null;
			while (true) {
				id = new Integer(((int) (Math.random() * 1000)) + 1000);
				if (!chats.containsKey(id))
					break;
			}
			dataChat dc = new dataChat(id, mcc.getNameChat(), mcc.getPasswort(), mcc.getIDClient(),
					clients.get(mcc.getIDClient()).getName(), mcastadr);
			chats.put(dc.getIDChat(), dc);
			System.out.println("Der Client " + dc.getNameCreater() + " mit der ID " + dc.getIDClient()
					+ " hat den Chat mit dem Namen " + dc.getChatname() + " und der Chat-ID " + dc.getIDChat()
					+ " erstellt");
			return MessageFactory.createCreateChatMsg(mcc.getNameChat(), mcc.getPasswort(), mcc.getIDClient(), true);

		}


		private Message deregisterClient(Message m) {
			if (m.getType() != MessageType.deregisterClient) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageDeregisterClient mdc = (MessageDeregisterClient) m;
			clients.remove(mdc.getIDClient());
			for (dataChat dc : chats.values()) {
				if (dc.getIDClient() == mdc.getIDClient()) {
					dc.setNameCreater("Unbekannt");
				}
			}

			System.out.println("Der Client mit der ID " + mdc.getIDClient() + " hat sich abgemeldet");
			return MessageFactory.createDeregisterClientMsg(0, true);

		}


		private Message enterChat(Message m) {
			if (m.getType() != MessageType.enterChat) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageEnterChat mec = (MessageEnterChat) m;

			// Überprüfung Client registriert?
			if (!clients.containsKey(mec.getIDClient())) {
				// falls nicht wird der fehlerhafte Teil hier die IDClient, da
				// nicht registriert, mit Default-Wert zurückgeschickt
				return MessageFactory.createEnterChatMsg(mec.getIDChat(), 0, mec.getPasswort(), null, false);
			}

			// Überprüfung existiert Chat
			if (!chats.containsKey(mec.getIDChat())) {
				// falls nicht wird der fehlerhafte Teil hier die IDClient, da
				// nicht registriert, mit Default-Wert zurückgeschickt
				return MessageFactory.createEnterChatMsg(0, mec.getIDClient(), mec.getPasswort(), null, false);
			}

			// Überprüfung Passwort
			dataChat dc = chats.get(mec.getIDChat());
			if (!dc.getPasswort().equals(mec.getPasswort())) {
				return MessageFactory.createEnterChatMsg(mec.getIDChat(), mec.getIDClient(), null, null, false);
			}

			dc.addNewEnteredClient(mec.getIDClient());

			System.out.println("Der Client mit der ID " + mec.getIDClient() + " ist dem Chat mit der ID "
					+ mec.getIDChat() + " beigetreten");
			return MessageFactory.createEnterChatMsg(mec.getIDChat(), mec.getIDClient(), mec.getPasswort(),
					dc.getMcastAdr(), true);

		}


		private Message sendMsgInChat(Message m) {
			if (m.getType() != MessageType.sendMsgInChat) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageSendMsgInChat msmic = (MessageSendMsgInChat) m;

			// Überprüfung Client registriert?
			if (!clients.containsKey(msmic.getIDClient())) {
				// falls nicht wird der fehlerhafte Teil hier die IDClient, da
				// nicht registriert, mit Default-Wert zurückgeschickt
				return MessageFactory.createSendMsgInChatMsg(0, msmic.getText(), msmic.getSender(), false);
			}

			// Überprüfung in Chat drinne?
			dataChat chat = null;
			for (dataChat dc : chats.values()) {
				if (dc.getEnteredClients().contains(msmic.getIDClient())) {
					chat = dc;
					break;
				}
				return MessageFactory.createSendMsgInChatMsg(msmic.getIDClient(), msmic.getText(), msmic.getSender(),
						false);
			}

			sendMCMessage(Util.getMessageAsDatagrammPacket(m, chat.getMcastAdr(), serverPort), chat.getMcastAdr());

			System.out.println("Der Client mit der ID " + msmic.getIDClient()
					+ " hat gerade eine Nachricht im Chat mit der ID " + chat.getIDChat() + " versendet");
			return MessageFactory.createSendMsgInChatMsg(msmic.getIDClient(), msmic.getText(), msmic.getSender(), true);

		}


		private Message requestChats(Message m) {
			if (m.getType() != MessageType.requestChats) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageRequestChats mrc = (MessageRequestChats) m;

			// Überprüfung Client registriert?
			if (!clients.containsKey(mrc.getClientID())) {
				// falls nicht wird der fehlerhafte Teil hier die IDClient, da
				// nicht registriert, mit Default-Wert zurückgeschickt
				return MessageFactory.createRequestChatsMsg(null, 0, false);
			}
			System.out.println("Der Client mit der ID " + mrc.getClientID() + " hat die verfügbaren Chats angefragt");
			return MessageFactory.createRequestChatsMsg(chats, mrc.getClientID(), true);

		}


		private Message leaveChat(Message m) {
			if (m.getType() != MessageType.leaveChat) {
				throw new RuntimeException("Typ passt nicht");
			}
			MessageLeaveChat mlc = (MessageLeaveChat) m;

			// Überprüfung Client registriert?
			if (!clients.containsKey(mlc.getIDClient())) {
				// falls nicht wird der fehlerhafte Teil hier die IDClient, da
				// nicht registriert, mit Default-Wert zurückgeschickt
				return MessageFactory.createLeaveChatMsg(0, mlc.getSender(), false);
			}

			// Überprüfung in Chat drinne?
			dataChat chat = null;
			for (dataChat dc : chats.values()) {
				if (dc.getEnteredClients().contains(mlc.getIDClient())) {
					chat = dc;
					break;
				}
				return MessageFactory.createLeaveChatMsg(mlc.getIDClient(), mlc.getSender(), false);
			}

			chat.removeNewEnteredClient(mlc.getIDClient());
			sendMCMessage(Util.getMessageAsDatagrammPacket(m, chat.getMcastAdr(), serverPort), chat.getMcastAdr());
			System.out.println("Der Client mit der Id " + mlc.getIDClient() + "hat gerade den Chat verlassen");
			return MessageFactory.createLeaveChatMsg(mlc.getIDClient(), mlc.getSender(), true);

		}
	}


	public void sendMCMessage(DatagramPacket dp, InetAddress mcastAdr) {
		try {
			multiSo.joinGroup(mcastAdr);
			multiSo.send(dp);
			multiSo.leaveGroup(mcastAdr);

			// TODO warte auf UDP Antworten der einzelnen Chats und sende
			// gebenfalls an ausstehende erneut
		} catch (IOException e) {
			e.printStackTrace();
			// TODO überprüfen: kann eigentlich nicht auftreten, da alle
			// Adressen vorgegeben sind
			throw new RuntimeException("Problem beim Senden der MulticastMessage");
		}
	}

}
