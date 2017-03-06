package messages.p2pmessages;

import main.UDPMessage;

public class PongMessage extends UDPMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3481847904366661690L;

	private String serviceName;
	private int port;
	
	public PongMessage(String serviceName, int port) {
		this.serviceName = serviceName;
		this.port = port;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getPort() {
		return port;
	}
	
	
	
	
}
