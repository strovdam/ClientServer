package a8;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import main.UDPMessage;
import messages.ChatMessage;
import messages.ChatStringMessage;

public class ChatServer {
	
	private DatagramSocket serverSocket;
	
	private List<String> history = new ArrayList<String>();
	private List<String> users = new ArrayList<String>();
	
	private boolean running;
	
	public ChatServer() {
		try {
			serverSocket = new DatagramSocket(4242);
		} catch (SocketException e) {
			System.out.println("Error during communication");
		}
		
	}
	
	private void receivedMessage(UDPMessage m) {
		if(m instanceof ChatMessage) {
			ChatMessage cm = (ChatMessage)m;
			String ms = "[" + cm.getTime() + "]<" + cm.getSender() + "> " + cm.getText();
			ChatStringMessage csm = new ChatStringMessage(ms);
			history.add(ms);
			System.out.println(ms);
			for(String u : users) {
				csm.send(serverSocket, u, 4243);
			}
		}
	}
	
	
	public void start() {
		running = true;
		while(running) {
			UDPMessage m = UDPMessage.fromSocket(serverSocket);
			if(!users.contains(m.getSenderIP())) {
				users.add(m.getSenderIP());
			}
			receivedMessage(m);
		}
	}
	
}
