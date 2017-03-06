package messages;

import main.UDPMessage;

public class ChatStringMessage extends UDPMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4772734573206036005L;
	private String message;

	public String getMessage() {
		return message;
	}

	public ChatStringMessage(String message) {
		super();
		this.message = message;
	}
	
	
}
