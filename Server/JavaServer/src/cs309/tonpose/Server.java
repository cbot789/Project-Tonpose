package cs309.tonpose;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class Server{
	
	private ArrayList<Thread> clients;
	private ServerSocket serverSocket = null;

	
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
		Server server = new Server(8080);
	}
}

class ClientHandler extends Thread {
	Socket s;
	Connection connection;
	ObjectInputStream in;
	ObjectOutputStream out;
	Message msg;

	ClientHandler(Socket s) {
		this.s = s;
		try {
			connection = DatabaseCommands.establishConnection();
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
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
			System.out.println("Client " + " sent a \"" + msg.getType() + "\" message");
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
		if(msg.getType().equals("checkUsername")){
			String username = msg.getData1();
			boolean exists;
			try {
				exists = DatabaseCommands.userExists(username, connection);
				if(exists){
					m.setData1("true");
				}
				else{
					m.setData1("false");
				}
				sendMsg(m);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(msg.getType().equals("checkUser")){
			String username = msg.getData1();
			String password = msg.getData2();
			boolean exists;
			try {
				exists = DatabaseCommands.userExists(username, password, connection);
				if(exists){
					m.setData1("true");
				}
				else{
					m.setData1("false");
				}
				sendMsg(m);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(msg.getType().equals("newUser")){
			String username = msg.getData1();
			String password = msg.getData2();
			try {
				boolean success = DatabaseCommands.insertUser(username, password, connection);
				if(success){
					m.setData1("true");
				}
				else{
					m.setData1("false");
				}
				sendMsg(m);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
