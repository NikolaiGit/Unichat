package message;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Message-Objekt zum Betreten von Chats. Werden Attribute im Default-Zustand
 * (null / 0) verschickt, so sind diese (mit Absicht) nicht gesetzt worden und
 * werden dies nur bei der Kommunikationrichtung Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageEnterChat extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String passwort;

	private final int IDChat;

	private final int IDClient;

	private final InetAddress multicastAdresse;


	protected MessageEnterChat(MessageType t, int IDChat, int IDClient, String p, InetAddress madr, boolean success) {
		type = t;
		this.IDChat = IDChat;
		this.IDClient = IDClient;
		passwort = p;
		multicastAdresse = madr;
		this.success = success;
	}


	/**
	 * @return the ID
	 */
	public int getIDChat() {
		return IDChat;
	}


	/**
	 * @return the passwort of the new Chat
	 */
	public String getPasswort() {
		return passwort;
	}


	/**
	 * @return the multicastAdresse
	 */
	public InetAddress getMulticastAdresse() {
		return multicastAdresse;
	}


	public int getIDClient() {
		return IDClient;
	}
}