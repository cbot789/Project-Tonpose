package cs309.tonpose;

import android.content.Intent;
import android.os.AsyncTask;

import java.net.*;
import java.io.*;

public class Client extends AsyncTask<Void, Void, Void>
{
	private Socket socket = null;
	private ObjectOutputStream streamOut = null;
	private ObjectInputStream streamIn = null;
	private String ip;
	private String username;
	private int port;
	protected String serverMsg;
	//private Message msg = null;
	private final String key = "132950YUDS9FH920U3Y4IDFJ3IRNMD0W";

	public Client(String ip, String username, int port) throws Exception
	{
		this.ip = ip;
		this.username = username;
		this.port = port;
	}

	protected Void doInBackground(Void... parm) {
		try{
			socket = new Socket(ip, port);
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//new ServerHandler().start();
		Message msg = new Message("username");
		msg.setData(username);
		try{
			streamOut.writeObject(msg);
			System.out.println("Sending "+ msg.getType() + " message");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		try{
			msg = (Message)streamIn.readObject();
			serverMsg = msg.getData();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
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

	private class ServerHandler extends Thread {
		public void run() {
			while(true){
				try{
					Message msg = (Message)streamIn.readObject();
					serverMsg = msg.getData();//TODO show(msg);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}




	/*
	@Override
	protected Void doInBackground(Void... parm) {													//new thread
		try {
			socket = new Socket(ip, port);
			//socket.connect(new InetSocketAddress(ip, port), 5000);									//attempt to open new socket to server, timeout after 3 seconds
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.writeObject(msg);																//send msg to server
			while(true){
				try{
					msg = (Message)streamIn.readObject();											//when received response save to msg and break
					break;
				}
				catch(NullPointerException e){								//do nothing and wait
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			msg = null;
		}finally{
			if(socket != null){
				try{																				//close socket when done
					socket.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void setMsg(Message message){															//set the message to be sent and encrypt it
		msg = message;
		//encrypt();
	}

	@Override
	protected void onPostExecute(Void results){														// when done with doInBackground
		if(msg != null){
			decrypt();
		}else{
			msg = new Message("Error");
			msg.setData("Timeout");
		}
		super.onPostExecute(results);
	}
	public Message getMessage(){																//returns message that was recieved
		return msg;
	}

	private void encrypt(){                   //encrypts type and data using the given key
		int length = msg.getType().length();
		StringBuffer buffer = new StringBuffer(msg.getType());
		int temp = 1;
		int currentKey = 1;
		int j = 0;

		for(int i = 0; i < key.length(); i++){					//encrypts type
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp * (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		String type = buffer.toString();
		length = msg.getData().length();
		buffer = new StringBuffer(msg.getData());
		j = 0;

		for(int i = 0; i < key.length(); i++){				//encrypts data
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp * (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		msg = new Message(type);
		msg.setData(buffer.toString());
	}

	private void decrypt() {                      //decrypts type and data using the given key
		int length = msg.getType().length();
		StringBuffer buffer = new StringBuffer(msg.getType());
		int temp = 1;
		int currentKey = 1;
		int j = 0;

		for(int i = 0; i < key.length(); i++){			//decrypts type
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp / (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		String type = buffer.toString();
		length = msg.getData().length();
		buffer = new StringBuffer(msg.getData());
		j = 0;

		for(int i = 0; i < key.length(); i++){			//decrypts data
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp / (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		msg = new Message(type);
		msg.setData(buffer.toString());
	}*/
}
