package tonpose;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server{
	
	private ArrayList<Thread> clients;
	private ServerSocket serverSocket = null;
	private Socket ClientSocket = null;

	
	public Server(int port){
		clients = new ArrayList<Thread>();
		
		try{
			serverSocket = new ServerSocket(port);
			System.out.println("Server started on port " + port);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

		while(true){
			Socket clientSocket = null;
			try{
				clientSocket = serverSocket.accept();
				System.out.println("Server connected to a client");
				Thread t = new Thread(new ClientHandler(clientSocket));
				clients.add(t);
				t.start();
			}
			catch(Exception e){
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		Server server = new Server(4444);
	}
}

class ClientHandler extends Thread {
	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	Message msg;
	String username;

	ClientHandler(Socket s) {
		this.s = s;
		try{
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			msg = (Message)in.readObject();
			username = msg.getData();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	public void run() {
		while(true){
			try{
				msg = (Message)in.readObject();
			}
			catch(Exception e){
				break;
			}
			handle(msg);
		}
	}
	public void sendMsg(Message m){
		try{
			out.writeObject(m);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void handle(Message msg){
		Message m = new Message("serverType");
		if(msg.getType().equals("type1")){
			String d = msg.getData();
			m.setData("You sent a type1 message that said: " + d);
			sendMsg(m);
		}
		if(msg.getType().equals("type2")){
			m.setData("You sent a type2 message. Stop sending type2 messages.");
			sendMsg(m);
		}
		else{
			String t = msg.getType();
			m.setData("You sent a "+ t + " message, I have no idea what to do with this.");
			sendMsg(m);
		}
	}
}
