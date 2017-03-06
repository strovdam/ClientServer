package a5;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import main.Message;
import messages.AcknowledgementMessage;
import messages.FileMessage;
import messages.StringMessage;

public class MainClient5 {
	public static void main(String[] args) {
		StringMessage m = new StringMessage("C:\\Users\\Damian\\Documents\\CAPCOM");
		try {
			Socket x = new Socket("127.0.0.1", 4242);
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			m.send(o);
			
			File f = new File("downloadedDirectory");
			if(!f.exists())
				f.mkdir();
			
			readFilesRecursive(i, "downloadedDirectory");
			
			x.close();
		} catch (UnknownHostException e) {
			System.out.println("Error during communication");
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
	}
	
	private static void readFilesRecursive(ObjectInputStream i, String currentPath) {
		int c = 0;
		boolean running = true;
		while(running) {
			Message m = Message.fromStream(i);
			if(m instanceof StringMessage) {
				File f = new File(currentPath + "\\" + ((StringMessage)m).getMsg());
				if(!f.exists())
					f.mkdir();
				readFilesRecursive(i, currentPath + "\\" + ((StringMessage)m).getMsg());
			} else if(m instanceof FileMessage) {
				try {
					Files.write(FileSystems.getDefault().getPath(currentPath + "\\" + c + ".txt"), ((FileMessage)m).getFileContents());
				} catch (IOException e) {
					System.out.println("Error during communication");
				}
				c++;
			} else if(m instanceof AcknowledgementMessage) {
				AcknowledgementMessage am = (AcknowledgementMessage)m;
				if(am.getPositive()) {
					running = false;
				} else {
					System.out.println("The file was not found");
					running = false;
				}
			}
		}
	}
}