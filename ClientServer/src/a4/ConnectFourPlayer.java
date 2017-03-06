package a4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.Message;
import messages.ConnectFourFieldMessage;
import messages.ConnectFourGameOverMessage;
import messages.ConnectFourMessage;
import messages.IntMessage;

public class ConnectFourPlayer {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream inp;
	
	private int playerIndex;
	
	public ConnectFourPlayer(int port) {
		try {
			this.socket = new Socket("127.0.0.1", port);
			out = new ObjectOutputStream(socket.getOutputStream());
			inp = new ObjectInputStream(socket.getInputStream());
			
			playerIndex = ((IntMessage) Message.fromStream(inp)).getValue();
		} catch (IOException e) { }
	}
	
	public void game() {
		new Thread(new ListeningClient()).start();
	}
	
	public void receivedInt(IntMessage mi) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int currentIndex = ((IntMessage) mi).getValue();
		if(currentIndex != playerIndex) {
			System.out.println("Enemy turn");
			return;
		}
		System.out.println("Your turn");
		String l = "";
		try {
			l = reader.readLine();
		} catch (IOException e) {
		}
		int x = Integer.parseInt(l);
		new ConnectFourMessage(x).send(out);
	}
	
	private class ListeningClient implements Runnable {

		@Override
		public void run() {
			while(true) {
				Message mi = Message.fromStream(inp);
				if(mi instanceof ConnectFourFieldMessage) {
					ConnectFourFieldMessage m = (ConnectFourFieldMessage) mi;
					ConnectFourField f = m.getField();
					clearConsole();
					f.print();
				}
				else if(mi instanceof IntMessage) {
					receivedInt((IntMessage)mi);
				} else if(mi instanceof ConnectFourGameOverMessage) {
					ConnectFourGameOverMessage m = (ConnectFourGameOverMessage)mi;
					if(m.getWinner() == (byte)playerIndex) {
						System.out.println("You won!");
					} else {
						System.out.println("You lost!");
					}
					break;
				}
			}
			try {
				socket.close();
			} catch (IOException e) {

			}
		}
		
	}

	private void clearConsole() {
		for(int i = 0; i < 30; i++) {
			System.out.println();
		}
	}
	
	
}
