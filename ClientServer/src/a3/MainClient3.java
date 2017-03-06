package a3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import main.Message;
import messages.FileMessage;
import messages.StringMessage;

public class MainClient3 {
	public static void main(String[] args) {
		String msg = "https://www.google.it";
		StringMessage m = new StringMessage(msg);
		try {
			Socket x = new Socket("127.0.0.1", 4242);
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			m.send(o);
			
			FileMessage fm = (FileMessage) Message.fromStream(i);
			Files.write(FileSystems.getDefault().getPath("ext.html"), fm.getFileContents());
			
			x.close();
		} catch (UnknownHostException e) {
			System.out.println("Error during communication");
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
	}
}