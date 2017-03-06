package a1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import main.Message;
import messages.CalculationMessage;
import messages.CalculationMessage.CalculationType;

public class MainClient1 {
	public static void main(String[] args) {
		double n1, n2, solution = 0;
		CalculationType type;
		Random rnd = new Random();
		n1 = rnd.nextInt(100);
		n2 = rnd.nextInt(100);
		type = CalculationType.values()[1 + rnd.nextInt(4)];
		CalculationMessage m = new CalculationMessage(n1, n2, type);
		try {
			Socket x = new Socket("127.0.0.1", 4242);
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			m.send(o);
			System.out.println("Sent messsage");
			m = (CalculationMessage) Message.fromStream(i);
			System.out.println("Received answer");
			solution = m.getSolution();
			x.close();
		} catch (UnknownHostException e) {
			System.out.println("Error during communication");
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
		System.out.println((int)n1 + " " + type + " " + (int)n2 + " = " + solution);
	}
}
