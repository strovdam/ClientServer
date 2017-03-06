package messages;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import main.Message;

public class FileMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8564958914939821178L;
	
	private String fileName;
	private byte[] fileContents;

	public FileMessage(File f) {
		fileName = f.getName();
		try {
			fileContents = Files.readAllBytes(FileSystems.getDefault().getPath(f.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getFileContents() {
		return fileContents;
	}
	
	
}
