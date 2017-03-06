package a12.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import messages.StringMessage;

public class CalculatePiService extends Service {

	public CalculatePiService() throws IOException {
		super("Pi Calculator", 42420);
		
	}

	@Override
	protected void handleService(ObjectOutputStream o, ObjectInputStream i) {
		int steps = 10000000;
		double pi = 3;
		for(int g = 1; g <= steps; g++) {
			double n = 4.0 / (2.0*g * (2.0*g + 1) * (2.0*g + 2)); 
			if(g % 2 == 1) {
				pi += n;
			} else {
				pi -= n;
			}
		}
		
		StringMessage m = new StringMessage("PI is about " + pi);
		m.send(o);
	}

}
