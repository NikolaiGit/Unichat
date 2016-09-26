package message;

import java.io.Serializable;
import java.net.SocketAddress;

public class dataClient implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int ID;
	private final String name;
	private final SocketAddress clientAdresse;


	public dataClient(int ClientID, String name, SocketAddress clientAdresse) {
		this.ID = ClientID;
		this.name = name;
		this.clientAdresse = clientAdresse;
	}


	public int getID() {
		return ID;
	}


	public SocketAddress getClientAdresse() {
		return clientAdresse;
	}


	public String getName() {
		return name;
	}
}
