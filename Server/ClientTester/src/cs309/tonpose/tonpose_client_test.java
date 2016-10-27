package cs309.tonpose;

import java.io.IOException;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

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
		name = "luke@email.com";
		String password = "abc123";
		client = new Client();
		client.start();

		//Register the client and packet classes
		Network.register(client);

		client.addListener(new Listener() {

			public void received (Connection connection, Object object) {
				if (object instanceof CheckUsername) {
					CheckUsername check = (CheckUsername)object;
					if(check.status){
						CheckUser check_user = new CheckUser();
						check_user.name = name;
						check_user.password = password;
						System.out.println("Sending CheckUser request for name: " + name);
						client.sendTCP(check_user);
					}
					else{
						NewUser new_user = new NewUser();
						new_user.name = name;
						new_user.password = password;
						System.out.println("Sending NewUser request for name: " + name);
						client.sendTCP(new_user);
					}
					System.out.println("Received \""+check.status+"\" response from server.");
				}
				if (object instanceof CheckUser) {
					CheckUser check = (CheckUser)object;
					if(check.status){
						System.out.println("Username and password are valid");
					}
					else{
						System.out.println("Username and password are valid");
					}
					System.out.println("Received \""+check.status+"\" response from server.");
				}
				if (object instanceof NewUser) {
					NewUser new_user = (NewUser)object;
					if(new_user.status){
						System.out.println("User added to database");
					}
					else{
						System.out.println("Error adding user");
					}
					System.out.println("Received \""+new_user.status+"\" response from server.");
				}
			}

		});

		
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, "10.25.70.122", Network.port); //10.25.70.122
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
				
				// Testing
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
		Log.set(Log.LEVEL_DEBUG);
		new tonpose_client_test();
	}
}
