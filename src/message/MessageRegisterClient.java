package message;

import java.io.Serializable;

/**
 * Message-Objekt zum Registrieren von Cients. Werden Attribute im
 * Default-Zustand (null / 0) verschickt, so sind diese (mit Absicht) nicht
 * gesetzt worden und werden dies nur bei der Kommunikationrichtung
 * Server->Client
 * 
 * @author Nikolai
 *
 */
public class MessageRegisterClient extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String name;

	private final int IDClient;


	protected MessageRegisterClient(MessageType t, String n, int id) {
		type = t;
		name = n;
		IDClient = id;

	}


	protected MessageRegisterClient(MessageType t, String n, int id, boolean success) {
		type = t;
		name = n;
		IDClient = id;
		this.success = success;

	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the iD
	 */
	public int getIDClient() {
		return IDClient;
	}
}
