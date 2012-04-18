package main;

import java.io.File;

import main.Main.AlgoType;

public class StegEngine {

	// вызывает метод соответствующего класса в зависимости от алгоритма
	
	public boolean encrypt(AlgoType algo, File file, String message) {
		switch (algo) {
		case ADDITIVE:
			AdditiveSteganography addCrypter=new AdditiveSteganography();
			return addCrypter.encrypt(file, message);
		case LSB:
			LSBSteganography lsbCrypter=new LSBSteganography();
			return lsbCrypter.encrypt(file, message);
		default:
			return false;
		}	
	}

	public boolean decrypt(AlgoType algo, File stegFile, File origin) {
		switch (algo) {
		case ADDITIVE:
			AdditiveSteganography addCrypter=new AdditiveSteganography();
			return addCrypter.decrypt(stegFile, origin);
		case LSB:
			LSBSteganography lsbCrypter=new LSBSteganography();
			return lsbCrypter.decrypt(stegFile, origin);
		default:
			return false;
		}
	}

}
