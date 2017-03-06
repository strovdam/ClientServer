package a6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import main.Message;
import messages.StringMessage;

public class SyncServerMain {
	
	public static void main(String[] args) {
		boolean running = true;
		ServerSocket server = null;
		try {
			server = new ServerSocket(4242);
		} catch (IOException e) {
			System.out.println("Error during communication");
			return;
		}
		while(running) {
			try {
				Socket s = server.accept();
				handleClient(s);
				s.close();
			} catch (IOException e) {
				System.out.println("Error during communication");
			}
		}
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		}

	}
	
	private static void handleClient(Socket s) throws IOException {
		ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream i = new ObjectInputStream(s.getInputStream());
		
		StringMessage sm = (StringMessage)Message.fromStream(i);
		
		if(sm.getMsg().equals("time")) {
			StringMessage m = new StringMessage(getTime());
			m.send(o);
		} else if(sm.getMsg().equals("random")) {
			StringMessage m = new StringMessage(getRandom());
			m.send(o);
		} else {
			StringMessage m = new StringMessage("Please insert time or random");
			m.send(o);
		}
	}
	
	private static String getTime() {
		sleepRandom();
		return new Date().toString();
	}
	
	private static String getRandom() {
		sleepRandom();
		int rnd = (int)(Math.random()*Integer.MAX_VALUE);
		return "" + rnd;
	}
	
	private static void sleepRandom() {
		try {
			Thread.sleep((int)(Math.random()*0000)+8000);
		} catch (InterruptedException e) {
			
		}
	}

}
