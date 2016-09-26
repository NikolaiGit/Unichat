/**
 * 
 */
package message;

/**
 * All available message types
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public enum MessageType {
	registerClient, deregisterClient, createChat, enterChat, leaveChat, requestChats, sendMsgInChat;
}
