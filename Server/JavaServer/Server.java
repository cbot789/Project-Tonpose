package JavaServer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server{

	public static void main(String[] args) throws IOException{

		ServerSocket serverSocket = null;
		
		try{
			serverSocket = new ServerSocket(4444);
			System.out.println("Server socket connection successful on port 4444");
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

		while(true){
			Socket clientSocket = null;
			try{
				System.out.println("Waiting for client");
				clientSocket = serverSocket.accept();

				System.out.println("Server connected to a client");
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
			}
			catch(Exception e){
				e.printStackTrace();
				serverSocket.close();
				System.exit(-1);
			}
		}
	}
}

class ClientHandler implements Runnable{
	Socket s;

	ClientHandler(Socket s){
		this.s = s;
	}

	public void run(){
		printSocketInfo(s);
		Scanner in;
		try{
			in = new Scanner(new BufferedInputStream(s.getInputStream())); 
			String msg = "";
			
			while(msg.equals("quit") == false){
				msg = in.nextLine();
				System.out.println("Client message: " + msg);
			}
			System.out.println("Client quit");
			in.close();
		}
		catch(Exception e){
		}
		
	}

	void printSocketInfo(Socket s){
		System.out.println("Server socket Local Address: " + s.getLocalAddress() + ":" + s.getLocalPort());
		System.out.println("Server socket Remote Address: " + s.getRemoteSocketAddress());
	}
}
