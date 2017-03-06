package a7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import main.Message;
import messages.StringMessage;

public class SyncServerMain {
	
	private static boolean running;
	
	public static void main(String[] args) {
		running = true;
		ServerSocket server = null;
		try {
			server = new ServerSocket(4242);
			server.setSoTimeout(1000);
		} catch (IOException e) {
			System.out.println("Error during communication");
			return;
		}
		new Thread(new ShutdownWaiter()).start();
		while(running) {
			try {
				Socket s = server.accept();
				handleClient(s);
				s.close();
			} catch (SocketTimeoutException e) {
				continue;
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
	
	private static class ShutdownWaiter implements Runnable {

		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Press enter for clean shutdown!");
			try {
				br.readLine();
			} catch (IOException e) {
				System.out.println("Error during communication");
			}
			running = false;
		}
		
	}

}
