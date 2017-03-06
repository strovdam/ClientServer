package a5;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.Message;
import messages.AcknowledgementMessage;
import messages.FileMessage;
import messages.StringMessage;

public class MainServer5 {
	
	public static void main(String[] args) {
		ExecutorService exe = Executors.newCachedThreadPool();
		ServerSocket s = null;
		boolean running = false;
		try {
			s = new ServerSocket(4242);
			running = true;
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
		while(running) {
			Socket x = null;
			try {
				x = s.accept();
			} catch (IOException e) {
				System.out.println("Error during communication");
			}
			exe.execute(new HandleClientThread(x));
		}
			
		try {
			s.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
	}

	private static class HandleClientThread implements Runnable {
		private Socket client;
		
		public HandleClientThread(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			ObjectInputStream i = null;
			ObjectOutputStream o = null;
			try {
				i = new ObjectInputStream(client.getInputStream());
				o = new ObjectOutputStream(client.getOutputStream());	
			} catch (IOException e) {
				System.out.println("Error during communication");
			}
			StringMessage m = (StringMessage) Message.fromStream(i);
			
			String path = m.getMsg();
			File file = new File(path);
			if(file.exists()) {
				sendDirRecursive(path, o);
				AcknowledgementMessage am = new AcknowledgementMessage(true);
				am.send(o);
			} else {
				AcknowledgementMessage am = new AcknowledgementMessage(false);
				am.send(o);
			}
		}	
		
		private void sendDirRecursive(String path, ObjectOutputStream o) {
			File file = new File(path);
			if(file.isFile()) {
				FileMessage fm = new FileMessage(new File(path));
				fm.send(o);
			} else if(file.isDirectory()) {
				String[] pathParts = path.split("\\\\");
				
				
				StringMessage sm = new StringMessage(pathParts[pathParts.length - 1]);
				sm.send(o);
				for(File f : file.listFiles()) {
					sendDirRecursive(f.getAbsolutePath(), o);
				}
				AcknowledgementMessage am = new AcknowledgementMessage(true);
				am.send(o);
			}
		}
	}
}
