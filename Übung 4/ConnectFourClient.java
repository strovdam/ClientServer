package a4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.Message;

public class ConnectFourClient {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream inp;
	
	public ConnectFourClient(Socket socket) {
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			inp = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Connection failed");
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void send(Message m) {
		try {
			out.reset();
		} catch (IOException e) { }
		m.send(out);
	}
	
	public Message read() {
		return Message.fromStream(inp);
	}
	
}
