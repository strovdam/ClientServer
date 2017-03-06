package messages;

import main.Message;

public class ConnectFourGameOverMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 268620064330603279L;
	
	private byte winner;
	
	public ConnectFourGameOverMessage(byte winner) {
		this.winner = winner;
	}

	public byte getWinner() {
		return winner;
	}
	
	

}
