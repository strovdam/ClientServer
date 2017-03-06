package messages;

import main.UDPMessage;

public class ChatMessage extends UDPMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2736721286060854722L;
	private String sender;
	private String time;
	private String text;
	public ChatMessage(String sender, String time, String text) {
		super();
		this.sender = sender;
		this.time = time;
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public String getTime() {
		return time;
	}
	public String getText() {
		return text;
	}
	
	
	
}
