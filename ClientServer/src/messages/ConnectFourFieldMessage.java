package messages;

import main.Message;
import a4.ConnectFourField;

public class ConnectFourFieldMessage extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4536757080171347962L;
	private ConnectFourField field;
	
	public ConnectFourFieldMessage(ConnectFourField field) {
		this.field = field;
	}
	
	public ConnectFourField getField() {
		return this.field;
	}
	
}
