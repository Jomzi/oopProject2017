package gmit.ie.sw;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="";
 	String ipaddress;
 	int port;
 	String downloadDir;
    Config config = null;
	Requester(){}
	void run()
	{
		config = new Config ("config.xml");
		config.parse();
		int selection = 0;
		
		while (selection != 4){
			selection = showMenu();
			
			switch(selection){
			case 1: 
				//Connect to server
				if (requestSocket != null && requestSocket.isConnected()){
					System.out.println("Client is already connected");
				}
				else{
					connectServer();
				}
				break;
			case 2:
				//Print File
				
				printFileListing();
				
				break;
			case 3:
				//Download File
				downloadFile();
				break;
			case 4:
				//Quit
				break;
				
			default:
				//handle exceptions
				break;
				
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
	
	public int showMenu(){
		
		
		
			System.out.println("1 Connect to server");
			System.out.println("2 Print file listing");
			System.out.println("3 Download file");
			System.out.println("4 Quit");
			
			Scanner stdin = new Scanner(System.in);
			
			return stdin.nextInt();
		
				
		
		
	}
	private void downloadFile(){
        System.out.println("Select a file.. ");
		
		Scanner stdin = new Scanner(System.in);
		String pathName = null;
		pathName = stdin.nextLine();
		
		
		try {					
			//outputs file size in console
			out.writeInt(3);
			out.writeObject(pathName);
			out.flush();
			
			Long downloadFileSize  = in.readLong();
			System.out.println("Size =  " + downloadFileSize);
			long totalByte = 0;
			byte[] buf = new byte[100];
			while(totalByte <= downloadFileSize){
				
				int bytes = in.read(buf);
				System.out.println("Read bytes..." + bytes);
			}
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	private void printFileListing(){
		
		
		
		
		int fileCount = 0;
		String fileName = null;
		
		
		try {					
			System.out.println("Sending files request.");
			out.writeInt(2);
			out.flush();
			
			try{
				fileCount = in.readInt();
				System.out.println("File count" + fileCount);
				for(int i =0; i< fileCount; i++){					
				
					fileName = (String) in.readObject();
					System.out.println("File Name: = " + fileName);
				}
			}
			catch(ClassNotFoundException cnf){
				System.err.println("Data received in unknown format");
			}
			
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	
	
	public void connectServer(){
		
		Scanner stdin = new Scanner(System.in);
		try{
			//1. creating a socket to connect to the server
			//System.out.println("Please Enter your IP Address");
			//ipaddress = stdin.next();
			ipaddress = config.getServerHost();
			port = Integer.parseInt(config.getServerPort());
			
			requestSocket = new Socket(ipaddress, port);
			System.out.println("Connected to "+ipaddress+" in port" + port);
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Hello");
			
			//3: Communicating with the server
			
			
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private void closeConnection(){
		try{
			in.close();
			out.close();
			requestSocket.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	
}