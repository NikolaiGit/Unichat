package message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Message-Objekt zur Erstellung von Chats. Werden Attribute im Default-Zustand
 * (null / 0) verschickt, so sind diese (mit Absicht) nicht gesetzt worden und
 * werden dies nur bei der Kommunikationrichtung Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageRequestChats extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HashMap<Integer, dataChat> chats;

	private final int ClientID;


	protected MessageRequestChats(MessageType t, HashMap<Integer, dataChat> chats, int ClientID) {
		type = t;
		this.chats = chats;
		this.ClientID = ClientID;

	}


	protected MessageRequestChats(MessageType t, HashMap<Integer, dataChat> chats, int ClientID, boolean success) {
		type = t;
		this.chats = chats;
		this.success = success;
		this.ClientID = ClientID;
	}


	/**
	 * @return the chats
	 */
	public HashMap<Integer, dataChat> getChats() {
		return chats;
	}


	public int getClientID() {
		return ClientID;
	}

}