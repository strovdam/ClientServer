package a1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import main.Message;
import messages.CalculationMessage;

public class MainServer1 {

	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(4242);
			Socket x = s.accept();
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
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
			s.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		} 
	}

}
