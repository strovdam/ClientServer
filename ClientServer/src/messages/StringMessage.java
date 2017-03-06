package messages;

import main.Message;

public class StringMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6415147709999349902L;
	private String msg;
	
	public StringMessage(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
