package message;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class dataChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int IDChat;
	private final String chatname;
	private final String passwort;
	private final InetAddress mcastadr;
	private final Set<Integer> enteredClients;
	private String nameCreater;
	/**
	 * ID des CLient, der den Chat erstellt hat
	 */
	private final int IDClient;


	public dataChat(int IDChat, String chatname, String passwort, int IDClient, String nameCreater,
			InetAddress mcastadr) {
		/**
		 * Id des Chats
		 */
		this.IDChat = IDChat;
		/**
		 * Name dieses Chats
		 */
		this.chatname = chatname;
		/**
		 * Passwort zu diesem Chat
		 */
		this.passwort = passwort;
		/**
		 * ID des Clients, der die Gruppe erstellt hat.
		 */
		this.IDClient = IDClient;

		this.nameCreater = nameCreater;

		/**
		 * Die MulticastAdresse dieses Chats. Wird in CreateChat erzeugt und
		 * dann in enterChat dem Client bekannt gegeben.
		 */
		this.mcastadr = mcastadr;
		/**
		 * hält die IDs der Clients, die gerade in diesem Chat drinne sind.
		 */
		enteredClients = new HashSet<>();
	}


	/**
	 * @return the iDChat
	 */
	public int getIDChat() {
		return IDChat;
	}


	/**
	 * @return the name
	 */
	public String getChatname() {
		return chatname;
	}


	/**
	 * @return the iDClient
	 */
	public int getIDClient() {
		return IDClient;
	}


	public String getPasswort() {
		return passwort;
	}


	public InetAddress getMcastAdr() {
		return mcastadr;
	}


	public boolean addNewEnteredClient(int IDClient) {
		return enteredClients.add(IDClient);
	}


	public boolean removeNewEnteredClient(int IDClient) {
		if (enteredClients.isEmpty())
			return false;
		return enteredClients.remove(IDClient);
	}


	public Set<Integer> getEnteredClients() {
		return enteredClients;
	}


	public String getNameCreater() {
		return nameCreater;
	}


	public void setNameCreater(String newName) {
		nameCreater = newName;
	}
}
