package a12;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import a12.services.Service;
import main.Message;
import main.UDPMessage;
import messages.StringMessage;
import messages.p2pmessages.PingMessage;
import messages.p2pmessages.PongMessage;

public class P2PClient {

	
	private DatagramSocket socket;
	
	private boolean searchingService;
	private List<ServiceProvider> serviceProviders;
	
	private List<Service> services;
	private boolean running;
	
	public P2PClient() {
		this(new ArrayList<>());
	}
	
	public P2PClient(List<Service> services) {
		this.services = services;
		running = true;
		serviceProviders = new ArrayList<>();
		try {
			socket = new DatagramSocket(9009);
		} catch (SocketException e) {
			System.out.println("Socket could not be created");
		}		
		
		if(offersServices()) {
			try {
				new Thread(new ServicePonger()).start();
			} catch (SocketException e) {
				System.out.println("Pong thread could not be created");
			}
		}
	}
	
	public void findServices() {
		serviceProviders.clear();
		PingMessage pm = new PingMessage();
		pm.broadcast(socket, 9010);
		
		searchingService = true;
		new Thread(new ServiceSearcher()).start();
	}
	
	public void stop() {
		running = false;
		stopSearching();
		socket.close();
		for(Service s : services)
			s.stop();
	}
	
	public void stopSearching() {
		searchingService = false;
	}
	
	public void requestService(ServiceProvider p) {
		stopSearching();
		new Thread(new ServiceUser(p)).start();
	}	
	
	public List<ServiceProvider> getServiceProviders() {
		return serviceProviders;
	}
	
	public boolean offersServices() {
		return services.size() > 0;
	}

	private class ServicePonger implements Runnable {
		private DatagramSocket pongSocket;
		
		public ServicePonger() throws SocketException {
			pongSocket = new DatagramSocket(9010);
		}

		@Override
		public void run() {
			while(running) {
				UDPMessage m = UDPMessage.fromSocket(pongSocket);
				if(m instanceof PingMessage) {
					for(Service s : services) {
						PongMessage pm = new PongMessage(s.getName(), s.getPort());
						pm.send(pongSocket, m.getSenderIP(), 9009);
					}
				}
			}
			pongSocket.close();
		}
		
		
	}

	private class ServiceUser implements Runnable {

		private ServiceProvider provider;
		
		public ServiceUser(ServiceProvider provider) {
			this.provider = provider;
		}
		
		@Override
		public void run() {
			Socket s = null;
			ObjectInputStream i = null;
			//ObjectOutputStream o = null;
			try {
				s = new Socket(provider.getIp(), provider.getPort());
				i = new ObjectInputStream(s.getInputStream());
				//o = new ObjectOutputStream(s.getOutputStream());
			} catch (UnknownHostException e) {
				System.out.println("Error during communication");
			} catch (IOException e) {
				System.out.println("Error during communication");
			}
			StringMessage sm = (StringMessage)Message.fromStream(i);
			System.out.println(sm.getMsg());
		}
		
	}

	private class ServiceSearcher implements Runnable {

		@Override
		public void run() {
			while(searchingService) {
				UDPMessage m = UDPMessage.fromSocket(socket);
				if(m instanceof PongMessage) {
					PongMessage pm = (PongMessage)m;
					ServiceProvider sp = new ServiceProvider(m.getSenderIP(), pm.getServiceName(), pm.getPort());
					serviceProviders.add(sp);
				}
			}
		}
		
	}
	
	public class ServiceProvider {
		private String ip;
		private String name;
		private int port;
		public ServiceProvider(String ip, String name, int port) {
			this.ip = ip;
			this.name = name;
			this.port = port;
		}
		public String getIp() {
			return ip;
		}
		public String getName() {
			return name;
		}
		public int getPort() {
			return port;
		}
	}
	
}
