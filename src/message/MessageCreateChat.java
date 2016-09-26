package message;

import java.io.Serializable;

/**
 * Message-Objekt zur Erstellung von Chats. Werden Attribute im Default-Zustand
 * (null / 0) verschickt, so sind diese (mit Absicht) nicht gesetzt worden und
 * werden dies nur bei der Kommunikationrichtung Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageCreateChat extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String nameChat;

	private final String passwort;

	private final int IDClient;


	protected MessageCreateChat(MessageType t, String nameChat, String passwort, int IDClient) {
		type = t;
		this.nameChat = nameChat;
		this.passwort = passwort;
		this.IDClient = IDClient;

	}


	protected MessageCreateChat(MessageType t, String nameChat, String passwort, int IDClient, boolean success) {
		type = t;
		this.nameChat = nameChat;
		this.passwort = passwort;
		this.IDClient = IDClient;
		this.success = success;

	}


	/**
	 * @return the name
	 */
	public String getNameChat() {
		return nameChat;
	}


	/**
	 * @return the passwort of the new Chat
	 */
	public String getPasswort() {
		return passwort;
	}


	public int getIDClient() {
		return IDClient;
	}
}