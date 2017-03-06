package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 312853181604924426L;
	public void send(ObjectOutputStream writer) {
		try {
			writer.writeObject(this);
			writer.flush();
		} catch (IOException e) {
		}
	}
	
	public static Message fromStream(ObjectInputStream reader) {
		try {
			Message m = (Message) reader.readObject();
			return m;
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
	
}
