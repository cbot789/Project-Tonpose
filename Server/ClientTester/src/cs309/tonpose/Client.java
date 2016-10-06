package cs309.tonpose;

import java.net.*;
import java.io.*;

public class Client
{
	private Socket socket = null;
	private ObjectOutputStream streamOut = null;
	private ObjectInputStream streamIn = null;
	private String username;
	protected String serverMsg;

	public Client(String ipAddr, String username, int serverPort)
	{
		this.username = username;
		try{
			socket = new Socket(ipAddr, serverPort);
			start();
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void sendMsg(Message msg)
	{
		try{
			streamOut.writeObject(msg);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void start() throws IOException
	{
		try{
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("client stream gen error");
		}
		new ServerHandler().start();
		Message msg = new Message("username");
		msg.setData1(username);
		try{
			streamOut.writeObject(msg);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	class ServerHandler extends Thread {
		public void run() {
			while(true){
				try{
					Message msg = (Message)streamIn.readObject();
					serverMsg = msg.getData2();//TODO show(msg);
				}
				catch(Exception e){
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}
}
