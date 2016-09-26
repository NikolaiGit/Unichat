package message;

import java.io.Serializable;

/**
 * Message-Objekt zum Betreten von Chats. Werden Attribute im Default-Zustand
 * (null / 0) verschickt, so sind diese (mit Absicht) nicht gesetzt worden und
 * werden dies nur bei der Kommunikationrichtung Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageLeaveChat extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int IDClient;
	private final String sender;


	protected MessageLeaveChat(MessageType t, int idclient, String sender) {
		type = t;
		IDClient = idclient;
		this.sender = sender;

	}


	protected MessageLeaveChat(MessageType t, int idclient, String sender, boolean success) {
		type = t;
		IDClient = idclient;
		this.sender = sender;
		this.success = success;

	}


	/**
	 * @return the iDClient
	 */
	public int getIDClient() {
		return IDClient;
	}


	public String getSender() {
		return sender;
	}
}