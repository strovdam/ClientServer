package a2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import main.Message;
import messages.AcknowledgementMessage;
import messages.AuthenticationMessage;
import messages.CalculationMessage;
import messages.CalculationMessage.CalculationType;

public class MainClient2 {
	public static void main(String[] args) {
		
		try {
			Socket x = new Socket("127.0.0.1", 4242);
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			
			routine(i, o);
			
			x.close();
		} catch (UnknownHostException e) {
			System.out.println("Error during communication");
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
	}
	
	private static void routine(ObjectInputStream i, ObjectOutputStream o) throws IOException {
		double n1, n2 = 0;
		CalculationType type;
		Random rnd = new Random();
		n1 = rnd.nextInt(100);
		n2 = rnd.nextInt(100);
		type = CalculationType.values()[1 + rnd.nextInt(4)];
		
		AuthenticationMessage a = new AuthenticationMessage("xyz2");
		a.send(o);
		
		AcknowledgementMessage ack = (AcknowledgementMessage) Message.fromStream(i);
		
		if(!ack.getPositive()) {
			System.out.println("Access denied");
			return;
		} else {
			System.out.println("Access granted");
		}
		
		CalculationMessage m = new CalculationMessage(n1, n2, type);
		m.send(o);
		m = (CalculationMessage) Message.fromStream(i);
		System.out.println(m.getN1() + " with " + m.getN2() + " = " + m.getSolution());
		
		
	}
}
