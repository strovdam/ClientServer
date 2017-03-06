package a11;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileClient {

	public static void main(String[] args) {
		 String ip;
		 if(args.length > 0)
			 ip = args[0];
		 else
			 ip = "192.168.2.10";
		 int port = 0;
		 if(args.length > 1) {
			 try {
				 port = Integer.parseInt(args[1]);
			 } catch (NumberFormatException e) {
				 System.out.println("The port format was wrong. Continuing with default port 4242.");
				 port = 4242;
			 }
		 } else {
			 port = 4242;
		 }
		 String file;
		 if(args.length > 2)
			 file = args[2];
		 else
			 file = "test.ext";
		 String[] fp = file.split("\\.");
		 if(fp.length == 1) {
			 file += ".ext";
		 } else if(fp.length > 2) {
			 System.out.println("This software only supports files with one single extension. All extensions have been removed.");
			 file = fp[0];
			 file += ".ext";
		 } else {
			 if(!file.endsWith(".ext")) {
				 file = file.substring(0, file.length() - 4);
				 file += "ext";
			 }
		 }
		 
		 String command = "GET " + file;
		 
		 try {
			Socket s = new Socket(ip, port);
			byte[] sendBuffer = new byte[1024];
			for(int i = 0; i < command.length(); i++) {
				sendBuffer[i] = (byte)command.charAt(i);
			}
			sendBuffer[command.length()] = 0;
			s.getOutputStream().write(sendBuffer);
			
			byte[] recvBuffer = new byte[1024];
			s.getInputStream().read(recvBuffer, 0, 1024);
			String ret = new String(recvBuffer).trim();
			
			File f = new File("temp.ext");
			if(!f.exists())
				f.createNewFile();
			PrintWriter pw = new PrintWriter(f);
			pw.print(ret);
			pw.close();
			
			System.out.println(ret);
			s.close();
		} catch (UnknownHostException e) {
			System.out.println("Connection to server failed. Did you use the right address and port?");
		} catch (IOException e) {
			System.out.println("Communication with server failed. Dis you insert the wrong IP Address or port or did you abort the connection?");
		}
		 
		 
	}
	
	
}
