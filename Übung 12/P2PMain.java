package a12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import a12.services.CalculatePiService;
import a12.services.Service;

public class P2PMain {

	public static void main(String[] args) {
		P2PClient c;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Would you like to offer the Calculate Pi Service? [y | N ] ");
		String l = "";
		try {
			l = br.readLine();
		} catch (IOException e2) {
			
		}
		ArrayList<Service> services = new ArrayList<>();
		if(l.equalsIgnoreCase("y")) {
			try {
				services.add(new CalculatePiService());
			} catch (IOException e) {
				System.out.println("Service creation failed. Do you already host another service?");
			}
		}
		c = new P2PClient(services);
		
		boolean redo = true;
		while(redo) {
			System.out.println("Press enter to start looking for Services (this will take about 3 seconds)");
			try {
				br.readLine();
			} catch (IOException e2) {
				
			}
			c.findServices();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e2) {
				
			}
			
			System.out.println("The following Services where found:");
			for(int i = 0; i < c.getServiceProviders().size(); i++) {
				System.out.println(i + " | " + c.getServiceProviders().get(i).getName());
			}
			System.out.println();
			System.out.print("Please enter the ID of the service you want to use(or leave blank to not use any service): ");
			try {
				l = br.readLine();
			} catch (IOException e1) {
				
			}
			if(!l.equals("")) {
				try {
					int x = Integer.parseInt(l);
					if(x >= 0 && x < c.getServiceProviders().size())
						c.requestService(c.getServiceProviders().get(x));
				} catch(NumberFormatException e) { }
			}
			
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e2) {
				
			}
			
			
			System.out.print("Would you like to discover services again? [y | N] ");
			try {
				l = br.readLine();
			} catch (IOException e) {
				
			}
			if(l.equalsIgnoreCase("Y")) {
				redo = true;
			} else {
				redo = false;
			}
		}
		c.stop();
		
	}

}
