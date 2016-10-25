package cs309.tonpose;

import java.io.IOException;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import cs309.tonpose.Network.Status;
import cs309.tonpose.Network.CheckUser;
import cs309.tonpose.Network.CheckUsername;
import cs309.tonpose.Network.MovePlayer;
import cs309.tonpose.Network.NewUser;
import cs309.tonpose.Network.PlayerConnect;
import cs309.tonpose.Network.PlayerDisconnect;
import cs309.tonpose.Network.UpdatePlayerList;

public class tonpose_client_test {
	Client client;
	String name;

	public tonpose_client_test () {
		client = new Client();
		client.start();

		//Register the client and packet classes
		Network.register(client);

		client.addListener(new Listener() {

			public void received (Connection connection, Object object) {
				if (object instanceof Status) {
					Status status = (Status)object;
					System.out.println("Received \""+status.status+"\" response from server.");
					return;
				}
			}

		});

		
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, "localhost", Network.port);
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
				
				// Testing
				String name = "luke@email.com";
				String password = "abc123";
				System.out.println("Sending CheckUsername request for name: " + name);
				CheckUsername check = new CheckUsername();
				check.name = name;
				client.sendTCP(check);
				
				Scanner in = new Scanner(System.in);
				while(name != "quit"){
					System.out.println("Enter name to check");
					name = in.next();
					check.name = name;
					client.sendTCP(check);
				}
				
		
				
				
			}
		}.start();
		
		
		
	}


	public static void main (String[] args) {
		//Log.set(Log.LEVEL_DEBUG);
		new tonpose_client_test();
	}
}
