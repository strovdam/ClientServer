package a2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import main.Message;
import messages.AcknowledgementMessage;
import messages.AuthenticationMessage;
import messages.CalculationMessage;

public class MainServer2 {

	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(4242);
			Socket x = s.accept();
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			AcknowledgementMessage ack;
			AuthenticationMessage aut;
			
			aut = (AuthenticationMessage) Message.fromStream(i);
			if(aut.getPassword().equals("xyz")) {
				ack = new AcknowledgementMessage(true);
				ack.send(o);
				CalculationMessage m = (CalculationMessage) Message.fromStream(i);
				double n1 = m.getN1();
				double n2 = m.getN2();
				double solution = 0;
				switch(m.getType()) {
				case ADDITION:
					solution = n1 + n2;
					break;
				case SUBTRACTION:
					solution = n1 - n2;
					break;
				case MULTIPLICATION:
					solution = n1 * n2;
					break;
				case DIVISION:
					solution = n1 / n2;
					break;
				case NONE:
					solution = Double.NEGATIVE_INFINITY;
					break;
				}
				m.setSolution(solution);
				m.send(o);
				
			} else {
				ack = new AcknowledgementMessage(false);
				ack.send(o);
			}
			s.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		} 
	}

}
