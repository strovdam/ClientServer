package messages;

import main.UDPMessage;

public class ChatLoginMessage extends UDPMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4595916577814746614L;
	private String username;

	public ChatLoginMessage(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	
}
