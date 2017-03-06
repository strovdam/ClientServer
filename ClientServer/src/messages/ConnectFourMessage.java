package messages;

import main.Message;

public class ConnectFourMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044350757505483354L;
	private int xIndex;
	private int yIndex;
	
	public ConnectFourMessage(int x, int y) {
		xIndex = x;
		yIndex = y;
	}
	
	public ConnectFourMessage(int x) {
		this(x, -1);
	}

	public int getxIndex() {
		return xIndex;
	}

	public int getyIndex() {
		return yIndex;
	}
	
	
}
