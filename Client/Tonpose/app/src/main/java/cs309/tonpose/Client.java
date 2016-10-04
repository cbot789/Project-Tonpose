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
	private int port;
	private Message msg = null;

	public Client(String ipAddr, int serverPort) throws Exception
	{
		ip = ipAddr;
		port = serverPort;
	}
	@Override
	protected Void doInBackground(Void... parm) {													//new thread
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(ip, port), 3000);
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.writeObject(msg);
			while(true){
				try{
					msg = (Message)streamIn.readObject();											//when recieve response save to msg and break
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
				try{
					socket.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	public void setMsg(Message message){
		msg = message;
	}

	@Override
	protected void onPostExecute(Void results){
		if(msg != null){
			msg.decrypt();
		}else{
			msg = new Message("Error");
			msg.setData("Timeout");
		}
		super.onPostExecute(results);
	}
	public Message getMessage(){
		return msg;
	}
}
