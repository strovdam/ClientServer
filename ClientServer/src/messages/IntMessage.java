package messages;

import main.Message;

public class IntMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4273235160563524059L;
	private int value;
	
	public IntMessage(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
