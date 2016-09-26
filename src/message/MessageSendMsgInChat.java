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
public class MessageSendMsgInChat extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int IDClient;
	private final String text;
	private final String sender;


	protected MessageSendMsgInChat(MessageType t, int idclient, String text, String sender) {
		type = t;
		IDClient = idclient;
		this.sender = sender;
		this.text = text;

	}


	protected MessageSendMsgInChat(MessageType t, int idclient, String text, String sender, boolean success) {
		type = t;
		IDClient = idclient;
		this.sender = sender;
		this.text = text;
		this.success = success;
	}


	/**
	 * @return the iDClient
	 */
	public int getIDClient() {
		return IDClient;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	public String getSender() {
		return sender;
	}

}