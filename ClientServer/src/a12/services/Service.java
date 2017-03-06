package a12.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service {

	private ServerSocket server;
	
	private String name;
	private int port;

	private boolean running;
	
	private ExecutorService clientThreads;
	private Thread acceptLoop;
	
	public Service(String name, int port) throws IOException {
		this.name = name;
		this.port = port;
		server = new ServerSocket(port);
		clientThreads = Executors.newCachedThreadPool();
		running = true;
		acceptLoop = new Thread(new AcceptClientsLoop());
		acceptLoop.start();
		
	}
	
	protected abstract void handleService(ObjectOutputStream o, ObjectInputStream i);
	
	public void stop() {
		running = false;
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}



	private class HandleClientLoop implements Runnable {
		private Socket socket;
		
		public HandleClientLoop(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			if(socket == null)
				return;
			try {
				ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
				handleService(o, i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private class AcceptClientsLoop implements Runnable {

		@Override
		public void run() {
			while(running) {
				Socket s = null;
				try {
					s = server.accept();
				} catch (IOException e) {
					if(running)
						e.printStackTrace();
				}
				
				clientThreads.execute(new HandleClientLoop(s));
			}
		}
		
	}
}
