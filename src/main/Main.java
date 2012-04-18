package main;

import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		//run();
		MainFrame frame=new MainFrame();
	}

	private static void run() {
		AlgoType algo = getAlgorythm();
		ActionType action = getAction();
		
		if(algo==null||action==null) showMessageAndExit("Some of parameters are invalid!");

		boolean result=false;
		StegEngine cryptoEngine=new StegEngine();
		
		if(action.equals(ActionType.ENCRYPT)){
			System.out.println("choose file to encrypt:");
			File file=getFileName();
			if(file==null) showMessageAndExit("file does not exists!");
			String message=getMessage();
			result=cryptoEngine.encrypt(algo, file, message);
		}else{
			System.out.println("choose encrypted file:");
			File file=getFileName();
			System.out.println("choose origin file:");
			File originFile=getFileName();
			if(originFile==null||file==null){
				showMessageAndExit("File does not exists!");
			}else{
				result=cryptoEngine.decrypt(algo, file, originFile);
			}
		}
		
		if(result){
			System.out.println("Work done!");
		}else{
			System.out.print("Work failed!");
		}
	}

	private static void showMessageAndExit(String message) {
		System.out.println(message);
		System.exit(0);
	}
	
	private static String getMessage(){
		System.out.println("Enter a message:");
		Scanner scanner=new Scanner(System.in);
		return scanner.nextLine();
	}

	private static ActionType getAction() {
		System.out.println("Enter an action:");
		System.out.println("1. Encrypt");
		System.out.println("2. Decrypt");

		try {
			Scanner scanner = new Scanner(System.in);
			int choise = scanner.nextInt();
			return ActionType.getTypeByCode(choise);
		} catch (Exception e) {
			System.out.println("Incorrect input!");
			return getAction();
		}
	}

	private static AlgoType getAlgorythm() {
		System.out.println("Enter an algorythm:");
		System.out.println("1. Additive");
		System.out.println("2. LSB");

		try {
			Scanner scanner = new Scanner(System.in);
			int choise = scanner.nextInt();
			return AlgoType.getTypeByCode(choise);
		} catch (Exception e) {
			System.out.println("Incorrect input!");
			return getAlgorythm();
		}
	}

	private static File getFileName() {
		System.out.println("Enter a file path:");
		Scanner scanner = new Scanner(System.in);
		String path = scanner.nextLine();
		File file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	// =========================================================
	// INNER CLASSES
	// =========================================================

	public enum ActionType {
		ENCRYPT(1), DECRYPT(2);
		@SuppressWarnings("unused")
		private int code;

		private ActionType(int code) {
			this.code = code;
		}

		private static ActionType getTypeByCode(int code) {
			switch (code) {
			case 1:
				return ActionType.ENCRYPT;
			case 2:
				return ActionType.DECRYPT;
			default:
				return null;
			}
		}
	}

	public enum AlgoType {
		ADDITIVE(1), LSB(2);
		@SuppressWarnings("unused")
		private int code;

		private AlgoType(int code) {
			this.code = code;
		}

		private static AlgoType getTypeByCode(int code) {
			switch (code) {
			case 1:
				return AlgoType.ADDITIVE;
			case 2:
				return AlgoType.LSB;
			default:
				return null;
			}
		}
	}

}
