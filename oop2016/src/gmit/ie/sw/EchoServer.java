package gmit.ie.sw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.util.concurrent.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.*;
public class EchoServer {
  public static void main(String[] args) throws Exception {
	  if(args.length != 2){
		  System.out.println("usage " + "java -cp .:./oop.jar ie.gmit.sw.Server 7777 /path/to/myfiles");
		  
	  }
	  else{
    ServerSocket m_ServerSocket = new ServerSocket(Integer.parseInt(args[0]),10);
    int id = 0;
    while (true) {
    	System.out.println("Waiting for a connection...");
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++, args[1]);
      cliThread.start();
    }
  }
}
}
class ClientServiceThread extends Thread {
  Socket clientSocket;
  String message;
  int clientID = -1;
  boolean running = true;
  ObjectOutputStream out;
  ObjectInputStream in;
  String currentDirectory = null;

  ClientServiceThread(Socket s, int i, String downloadDir) {
    clientSocket = s;
    clientID = i;
    currentDirectory = downloadDir;
  }

  void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  public void run() {
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try 
    {
    	out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		      + clientSocket.getInetAddress().getHostName());
		
		//sendMessage("Connection successful");
		String operation;
		do{
		
			try {
				int op = in.readInt();
				System.out.println("op: = " + op);
				
				if(op == 2)
				{
					System.out.println("Reading path...");
				
					
					File directory = new File(currentDirectory);
					if(directory.exists()){
						
					   	File[]fileList = directory.listFiles();
					   	int fileCount = 0;
					   	for(int i =0; i< fileList.length; i++){
					   		
					   		if (fileList[i].isFile()){
					   			fileCount ++;
					   			
					   		}
					   	}
					   	
					   	System.out.println("File count" + fileCount);
						
					   	out.writeInt(fileCount);
					   	
					   	for(int i =0; i< fileList.length; i++){
					   		
					   		if (fileList[i].isFile()){
					   			sendMessage(fileList[i].getName());					   			
					   		}
					   	}
					} else {
						System.out.println("Directory " + currentDirectory + " doesn't exist.");
					}
					
				}
				else if(op == 3){
					//gets file size/ outputs to console
					System.out.println("Reading file name...");
					String filePath = (String) in.readObject();
					System.out.println("File: " + filePath);
					filePath = currentDirectory + "\\" + filePath;
					File downloadFile = new File(filePath);
					long size;
					if(downloadFile.exists()){
						size = downloadFile.length();
						downloadFile.length();
						System.out.println("Output file size... " + size);
						out.writeLong(size);
						out.flush();
					}
					else{
						System.out.println("File doesn't exists!");
					}
					Path file = Paths.get(filePath);
					try (SeekableByteChannel sbc = Files.newByteChannel(file)) {
					    ByteBuffer buf = ByteBuffer.allocate(100);
					    while (sbc.read(buf) > 0) {
					    	System.out.println("Sending... "+ buf.array().length);
					    	buf.rewind();
					    	out.write(buf.array());
					    	
					    }
					
					
				}
				
				}
			}
			catch(IOException io){
				System.err.println("Data received in unknown format");
			}
			
			operation = "0";
			Thread.sleep(200);
			
    	}while(operation == ("0"));
      
		System.out.println("Ending Client : ID - " + clientID + " : Address - "
		      + clientSocket.getInetAddress().getHostName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}