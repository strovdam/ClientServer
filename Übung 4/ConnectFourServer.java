package a4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import main.Message;
import messages.ConnectFourFieldMessage;
import messages.ConnectFourGameOverMessage;
import messages.ConnectFourMessage;
import messages.IntMessage;

public class ConnectFourServer {

	private boolean running;
	private ServerSocket server;
	private List<ConnectFourClient> clients = new ArrayList<ConnectFourClient>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private int currentPlayer = 1;
	private ConnectFourField field = new ConnectFourField();
	
	public ConnectFourServer() {
		running = true;
		try {
			server = new ServerSocket(4242);
		} catch (IOException e) {
			System.out.println("Could not create Server");
			return;
		}
		executor.execute(new ClientListener());
	}
	
	private void handleInput(ConnectFourClient c, Message m) {
		if(m instanceof ConnectFourMessage) {
			ConnectFourMessage cm = (ConnectFourMessage)m;
			if(clients.indexOf(c) == currentPlayer - 1) {
				field.fall(cm.getxIndex(), currentPlayer);
				if(field.checkWinner() != 0) {
					win(field.checkWinner());
					return;
				}
				broadcast(new ConnectFourFieldMessage(field));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				currentPlayer = (currentPlayer % 2) + 1;
				broadcast(new IntMessage(currentPlayer));
			}
		}
	}
	
	private void win(byte winner) {
		broadcast(new ConnectFourGameOverMessage(winner));
		running = false;
		executor.shutdown();
		try {
			server.close();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	
	private class ClientListener implements Runnable {
		public void run() {
			while(running) {
				Socket s = null;
				try {
					s = server.accept();
				} catch (IOException e) {
					
				}
				ConnectFourClient c = new ConnectFourClient(s);
				clients.add(c);
				executor.execute(new ClientHandler(c));
				if(clients.size() == 2) {
					clients.get(0).send(new IntMessage(1));
					clients.get(1).send(new IntMessage(2));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					broadcast(new IntMessage(1));
					break;
				}
			}
		}
	}
	
	private void broadcast(Message m) {
		for(ConnectFourClient c : clients) {
			c.send(m);
		}
	}
	
	private class ClientHandler implements Runnable {
		
		private ConnectFourClient client;
		
		public ClientHandler(ConnectFourClient c) {
			client = c;
		}
		
		public void run() {
			while(running) {
				Message msg = client.read();
				handleInput(client, msg);
			}
		}
		
	}
	
}
