package message;

import java.io.Serializable;

/**
 * Message-Objekt zum Abmelden Clienten. Werden Attribute im Default-Zustand
 * (null / 0) verschickt, so sind diese (mit Absicht) nicht gesetzt worden und
 * werden dies nur bei der Kommunikationrichtung Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageDeregisterClient extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int IDClient;


	protected MessageDeregisterClient(MessageType t, int id) {
		type = t;
		IDClient = id;

	}


	protected MessageDeregisterClient(MessageType t, int id, boolean success) {
		type = t;
		IDClient = id;
		this.success = success;
	}


	/**
	 * @return the iD
	 */
	public int getIDClient() {
		return IDClient;
	}
}
