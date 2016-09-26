package Client;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

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

public class Client implements ClientIF {
	private InetAddress serverAdresse;
	private int serverPort = 55555;
	private int localPort = 55555;
	private String name;
	private int IDClient;
	private List<String> content = null;
	private WaitForMessage messageWaiter;
	private ClientGUI gui;
	/**
	 * Liste der möglichen Chats
	 */
	private HashMap<Integer, dataChat> chats;
	/**
	 * MulticastAdresse des Chats
	 */
	private InetAddress multicastAdresse;
	/**
	 * falls != null, dann in diesem Chat
	 */
	private dataChat enteredChat = null;

	/**
	 * ob der Client schon am Server registriert ist
	 */
	private boolean isRegistered = false;


	public Client() {
		content = Collections.synchronizedList(new LinkedList<String>());
	}


	@Override
	public boolean registerClient(String name, String adresse) throws IOException {

		this.name = name;
		try {
			this.serverAdresse = InetAddress.getByName(adresse.trim());
			if (!Util.testConnection(serverAdresse, serverPort, 1000))
				throw new IOException("Kein Server unter der angegebenen Adresse erreichbar");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new IOException("Kein Server unter der angegebenen Adresse erreichbar");
		}

		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createRegisterClientMsg(name, 0, false), serverAdresse,
					serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (resp.getType() != MessageType.registerClient) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageRegisterClient mrc = (MessageRegisterClient) resp;
		this.IDClient = mrc.getIDClient();

		return isRegistered = mrc.getSuccess();
	}


	@Override
	public boolean requestChats() {
		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createRequestChatsMsg(null, this.IDClient, false),
					serverAdresse, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (resp.getType() != MessageType.requestChats) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageRequestChats mgc = (MessageRequestChats) resp;
		this.chats = mgc.getChats();
		return mgc.getSuccess();
	}


	@Override
	public boolean enterChat(dataChat chat, String passwort) {
		// Passwort überprüfen
		if (!chat.getPasswort().equals(passwort)) {
			return false;
		}
		Message resp;
		try {
			resp = Util.sendAndGetMessage(
					MessageFactory.createEnterChatMsg(chat.getIDChat(), this.IDClient, passwort, null, false),
					serverAdresse, serverPort);
		} catch (IOException e) {
			return false;
		}

		if (resp.getType() != MessageType.enterChat) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageEnterChat mec = (MessageEnterChat) resp;
		this.multicastAdresse = mec.getMulticastAdresse();

		if (!startReceivingMessages()) {
			// TODO Dann erst noch abmelden?
			return false;
		}

		if (mec.getSuccess()) {
			enteredChat = chat;
			return true;
		} else {
			return false;
		}
	}


	private boolean startReceivingMessages() {
		try {
			messageWaiter = new WaitForMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		Thread t = new Thread(messageWaiter);
		t.start();
		return true;
	}


	@Override
	public boolean sendMessageInChat(String text) {
		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createSendMsgInChatMsg(this.IDClient, text, name, false),
					serverAdresse, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (resp.getType() != MessageType.sendMsgInChat) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageSendMsgInChat msmic = (MessageSendMsgInChat) resp;
		return msmic.getSuccess();
	}


	@Override
	public boolean createChat(String name, String passwort) {
		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createCreateChatMsg(name, passwort, this.IDClient, false),
					serverAdresse, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (resp.getType() != MessageType.createChat) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageCreateChat mcc = (MessageCreateChat) resp;

		if (mcc.getSuccess()) {
			// aktualisiert eigene Liste aller verfügbaren Chats
			requestChats();
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean leaveChat() {
		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createLeaveChatMsg(this.IDClient, this.name, false),
					serverAdresse, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (resp.getType() != MessageType.leaveChat) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageLeaveChat mlc = (MessageLeaveChat) resp;

		if (!stopReceivingMessages())
			return false;

		if (mlc.getSuccess()) {
			enteredChat = null;
			return true;
		} else {
			return false;
		}
	}


	private boolean stopReceivingMessages() {

		messageWaiter.stopThread();
		return true;
	}


	@Override
	public boolean deregisterClient() {
		Message resp;
		try {
			resp = Util.sendAndGetMessage(MessageFactory.createDeregisterClientMsg(this.IDClient, false), serverAdresse,
					serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (resp.getType() != MessageType.deregisterClient) {
			throw new RuntimeException("Typ passt nicht");
		}
		MessageDeregisterClient mdc = (MessageDeregisterClient) resp;

		if (mdc.getSuccess()) {
			isRegistered = false;
			return true;
		} else {
			return false;
		}
	}


	@Override
	public dataChat getEnteredChat() {
		return enteredChat;
	}


	@Override
	public boolean isRegistered() {
		return isRegistered;
	}


	@Override
	public HashMap<Integer, dataChat> getChats() {
		return this.chats;
	}


	@Override
	public List<String> getNewMessages() {
		List<String> hilf = new LinkedList<>();
		hilf.addAll(content);
		content.clear();
		return hilf;
	}


	@Override
	public void setGUI(ClientGUI cgui) {
		this.gui = cgui;
	}

	/**
	 * 
	 * Klasse wartet auf neue Nachrichten
	 *
	 */
	private class WaitForMessage implements Runnable {
		private MulticastSocket multicastSocket;
		private DatagramSocket udpSocket;
		private boolean isRunning = true;


		public WaitForMessage() throws IOException {
			try {
				multicastSocket = new MulticastSocket(localPort);
				multicastSocket.joinGroup(multicastAdresse);
				udpSocket = new DatagramSocket(localPort + 1);
			} catch (BindException e) {
				JOptionPane.showMessageDialog(null, new String("Unichat läuft bereits\n\nFehlercode: 1"), "Fehler",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}


		/**
		 * Prevents the thread in run() to do another iteration of its action.
		 * 
		 * @return true if the thread has been stopped, false if the thread was
		 *         already stopped
		 */
		public boolean stopThread() {
			multicastSocket.close();
			if (isRunning) {
				isRunning = false;
				return true;
			} else
				return false;
		}


		@Override
		public void run() {
			byte[] buffer = new byte[65508];// max size of a DatagramPacket
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, serverAdresse, serverPort);
			while (isRunning) {
				try {
					multicastSocket.receive(datagramPacket);
					Message receivedMessage = Util.getMessageOutOfDatagramPacket(datagramPacket);

					switch (receivedMessage.getType()) {
					case sendMsgInChat:
						MessageSendMsgInChat msmic = (MessageSendMsgInChat) receivedMessage;
						// TODO was machen wir jetzt mim Text

						content.add(msmic.getSender() + "\n" + msmic.getText());

						// antwortet mit selbem MessageTyp und eigener ID, dass
						// er die MulticastNachricht empfangen hat
						udpSocket.send(Util.getMessageAsDatagrammPacket(
								MessageFactory.createSendMsgInChatMsg(IDClient, null, null, true), serverAdresse,
								serverPort));
						gui.repaintMessagePanel();
						break;
					case leaveChat:
						MessageLeaveChat mlc = (MessageLeaveChat) receivedMessage;
						// TODO was machen wir hiermit
						content.add(mlc.getSender() + " hat den Chat verlassen");
						// antwortet mit selbem MessageTyp und eigener ID, dass
						// er die MulticastNachricht empfangen hat
						udpSocket.send(Util.getMessageAsDatagrammPacket(
								MessageFactory.createLeaveChatMsg(IDClient, name, true), serverAdresse, serverPort));
						gui.repaintMessagePanel();
						break;
					default:
						throw new RuntimeException("Wrong MessageType");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
