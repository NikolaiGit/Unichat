/**
 * 
 */
package message;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;

/**
 * Factory class used to produce messages
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 * @Idea: Eigene MessageFactory in Server und einer in Client mit
 *        voreingestelleten Variablenwerten wenn null / 0
 */
public class MessageFactory {

	public static Message createRegisterClientMsg(String name, int id, boolean success) {
		return new MessageRegisterClient(MessageType.registerClient, name, id, success);
	}


	public static Message createDeregisterClientMsg(int id, boolean success) {
		return new MessageDeregisterClient(MessageType.deregisterClient, id, success);
	}


	public static Message createCreateChatMsg(String name, String passwort, int IDClient, boolean success) {
		return new MessageCreateChat(MessageType.createChat, name, passwort, IDClient, success);
	}


	public static Message createEnterChatMsg(int IDChat, int IDClient, String passwort, InetAddress multicastAdresse,
			boolean success) {
		return new MessageEnterChat(MessageType.enterChat, IDChat, IDClient, passwort, multicastAdresse, success);
	}


	public static Message createLeaveChatMsg(int IDClient, String sender, boolean success) {
		return new MessageLeaveChat(MessageType.leaveChat, IDClient, sender, success);
	}


	public static Message createRequestChatsMsg(HashMap<Integer, dataChat> chats, int clientID, boolean success) {
		return new MessageRequestChats(MessageType.requestChats, chats, clientID, success);
	}


	public static Message createSendMsgInChatMsg(int IDClient, String text, String sender, boolean success) {
		return new MessageSendMsgInChat(MessageType.sendMsgInChat, IDClient, text, sender, success);
	}

}
