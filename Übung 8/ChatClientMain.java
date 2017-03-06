package a8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;


import main.UDPMessage;
import messages.ChatLoginMessage;
import messages.ChatMessage;
import messages.ChatStringMessage;

public class ChatClientMain {

	public static void main(String[] args) {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(4243);
		} catch (SocketException e) {
			System.out.println("Error during communication");
		}
		String ip = "127.0.0.1";
		int port = 4242;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Please enter your username: ");
		String username = "";
		try {
			username = br.readLine();
		} catch (IOException e1) {

		}
		
		new Thread(new ReadFromServerLoop(clientSocket)).start();
		new ChatLoginMessage(username).send(clientSocket, ip, port);
		
		boolean running = true;
		System.out.println("Welcome to the Chat! Type /EXIT to exit!");
		while(running) {
			String l = "";
			try {
				l = br.readLine();
			} catch (IOException e) {
			}
			if(l.equals("/EXIT")) {
				running = false;
			} else {
				ChatMessage cm = new ChatMessage(username, new Date().toString(), l);
				cm.send(clientSocket, ip, port);
			}
		}
		System.exit(0);
		
	}
	
	private static class ReadFromServerLoop implements Runnable {
		private DatagramSocket clientSocket;
		
		public ReadFromServerLoop(DatagramSocket s) {
			clientSocket = s;
		}
		
		
		@Override
		public void run() {
			while(true) {
				ChatStringMessage m = (ChatStringMessage)UDPMessage.fromSocket(clientSocket);
				System.out.println(m.getMessage());
			}
		}
		
		
		
	}

}
