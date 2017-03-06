package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public abstract class UDPMessage implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String senderIP;

	public void send(DatagramSocket socket, String ip, int port) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(bo);
			o.writeObject(this);
			o.flush();
			byte[] msg = bo.toByteArray();
			
			byte[] helper = new byte[4];
			for (int i = 0; i < 4; ++i) {
		          int shift = i << 3; // i * 8
		          helper[3-i] = (byte)((msg.length & (0xff << shift)) >>> shift);
		    }
			DatagramPacket p = new DatagramPacket(helper, 0, helper.length, InetAddress.getByName(ip), port);
			socket.send(p);
			
			p = new DatagramPacket(msg, 0, msg.length, InetAddress.getByName(ip), port);
			socket.send(p);
		} catch (IOException e) {
		}
	}
	
	public void broadcast(DatagramSocket socket, int port) {
		try {
			String ip = getBroadcast();
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(bo);
			o.writeObject(this);
			o.flush();
			byte[] msg = bo.toByteArray();
			
			byte[] helper = new byte[4];
			for (int i = 0; i < 4; ++i) {
		          int shift = i << 3; // i * 8
		          helper[3-i] = (byte)((msg.length & (0xff << shift)) >>> shift);
		    }
			DatagramPacket p = new DatagramPacket(helper, 0, helper.length, InetAddress.getByName(ip), port);
			socket.send(p);
			
			p = new DatagramPacket(msg, 0, msg.length, InetAddress.getByName(ip), port);
			socket.send(p);
		} catch (IOException e) {
		}
	}
	
	public static UDPMessage fromSocket(DatagramSocket socket) {
		try {
			byte[] helper = new byte[4];
			DatagramPacket p = new DatagramPacket(helper, 0, helper.length);
			socket.receive(p);
			
			int len = 0;
			for (int i = 0; i < 4; ++i) {
				len |= (helper[3-i] & 0xff) << (i << 3);
			}
			
			byte[] buffer = new byte[len];
			p = new DatagramPacket(buffer, 0, buffer.length);
			socket.receive(p);

			ByteArrayInputStream bi = new ByteArrayInputStream(p.getData());
		    ObjectInputStream i = new ObjectInputStream(bi);
		    
		    UDPMessage m = (UDPMessage)i.readObject();	
		    m.setSenderIP(p.getAddress().getHostAddress());
		    return m;
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}

	public String getSenderIP() {
		return senderIP;
	}

	public void setSenderIP(String senderIP) {
		this.senderIP = senderIP;
	}
	
	private static String getBroadcast() {
		Enumeration<NetworkInterface> en = null;
		try {
			en = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			
		}
	    while (en.hasMoreElements()) {
	      NetworkInterface ni = en.nextElement();

	      List<InterfaceAddress> list = ni.getInterfaceAddresses();
	      Iterator<InterfaceAddress> it = list.iterator();

	      while (it.hasNext()) {
	        InterfaceAddress ia = it.next();
	        
	        if(ia.getBroadcast() != null && !ia.getBroadcast().toString().startsWith("/127")) {
	        	return ia.getBroadcast().getHostAddress();
	        }
	      }
	    }
	    return "127.255.255";
	}
	
	
}
