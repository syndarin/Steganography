package main;

import java.io.File;

public interface CryptMachine {
	
	boolean encrypt(File file, String message);
	boolean decrypt(File stegFile, File origin);

}
