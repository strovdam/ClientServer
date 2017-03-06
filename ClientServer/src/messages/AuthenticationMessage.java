package messages;

import main.Message;

public class AuthenticationMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7060099576819967808L;
	
	private String password;
	
	public AuthenticationMessage(String pw) {
		this.password = pw;
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
