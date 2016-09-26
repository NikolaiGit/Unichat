package Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import message.dataChat;

public interface ClientIF {

	/**
	 * Registriert den Client an dem Server
	 * 
	 * @param name
	 *            Name des Clients
	 * @param adresse
	 *            Adresse des Servers
	 * @return
	 */
	public boolean registerClient(String name, String adresse) throws IOException;


	/**
	 * 
	 */
	public boolean requestChats();


	/**
	 * 
	 */
	public boolean enterChat(dataChat chat, String passwort);


	/**
	 * 
	 */
	public boolean sendMessageInChat(String text);


	/**
	 * 
	 */
	public boolean createChat(String name, String passwort);


	/**
	 * 
	 */
	public boolean leaveChat();


	/**
	 * 
	 */
	public boolean deregisterClient();


	/**
	 * 
	 */
	public HashMap<Integer, dataChat> getChats();


	/**
	 * 
	 * @return
	 */
	public dataChat getEnteredChat();


	/**
	 * 
	 * @return
	 */
	public boolean isRegistered();


	/**
	 * 
	 * @return
	 */
	public List<String> getNewMessages();


	void setGUI(ClientGUI cgui);

}
