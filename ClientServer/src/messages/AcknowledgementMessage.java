package messages;

import main.Message;

public class AcknowledgementMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7833216613709112663L;
	
	
	private boolean positive;
	
	public AcknowledgementMessage(boolean pos) {
		positive = pos;
	}
	
	public boolean getPositive() {
		return positive;
	}
}
