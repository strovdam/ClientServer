package a3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import main.Message;
import messages.FileMessage;
import messages.StringMessage;

public class MainServer3 {

	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(4242);
			Socket x = s.accept();
			ObjectInputStream i = new ObjectInputStream(x.getInputStream());
			ObjectOutputStream o = new ObjectOutputStream(x.getOutputStream());
			StringMessage m = (StringMessage) Message.fromStream(i);
			System.out.println(m.getMsg());
			URL url = new URL(m.getMsg());
			InputStream is = url.openStream();
			
			Files.copy(is, FileSystems.getDefault().getPath("out.html"), StandardCopyOption.REPLACE_EXISTING);
			
			FileMessage fm = new FileMessage(new File("out.html"));
			fm.send(o);
			s.close();
		} catch (IOException e) {
			System.out.println("Error during communication");
		}
	}

}
