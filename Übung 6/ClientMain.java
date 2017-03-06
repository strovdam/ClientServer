package a6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import main.Message;
import messages.StringMessage;

public class ClientMain {

	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream i = null;
		ObjectOutputStream o = null;
		System.out.print("Please choose an action [ time | random ]: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String l = "";
		try {
			l = br.readLine();
		} catch (IOException e) {
			
		}
		long startTime = Calendar.getInstance().getTimeInMillis();
		try {
			socket = new Socket("127.0.0.1", 4242);
			i = new ObjectInputStream(socket.getInputStream());
			o = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("Error during communication");
			return;
		} catch (IOException e) {
			System.out.println("Error during communication");
			return;
		}
		StringMessage sm = new StringMessage(l);
		sm.send(o);
		StringMessage answer = (StringMessage)Message.fromStream(i);
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(answer.getMsg());
		System.out.println("You have been waiting for " + (endTime - startTime) + " milliseconds");
		
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
		
	}
	
	
}
