/**
 * 
 */
package message;

import java.io.Serializable;

/**
 * Container to be sent containing specific payloads
 * 
 * @author Nikolai Seip
 *
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * type of the message to indicate how to handle with this message
	 */
	protected MessageType type;

	/**
	 * gibt in der Antwortmessage an, ob die Operation erfolgreich war
	 */
	protected boolean success = false;

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	public void setSuccess() {
		success = true;
	}

	public boolean getSuccess() {
		return success;
	}
}
