package cs309.tonpose;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

//Server packet classes
public class Network {
	static public final int port = 8080;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String[].class);
		kryo.register(CheckUsername.class);
		kryo.register(CheckUser.class);
		kryo.register(NewUser.class);
		kryo.register(ClientConnect.class);
		kryo.register(AddUser.class);
		kryo.register(UpdateUser.class);
		kryo.register(RemoveUser.class);
		kryo.register(MovePlayer.class);
		kryo.register(User.class);
	}
	
	// For checking if username is in database
	static public class CheckUsername {
		public String name;
		public boolean status;
	}

	// For checking if Username and Password match in database
	static public class CheckUser {
		public String name, password;
		public boolean status;
	}

	// For creating a new account in database
	static public class NewUser {
		public String name, password;
		public boolean status;
	}

	// For Connecting a client to the game
	static public class ClientConnect {
		public String name;
		public int id;
		public float x, y;
	}

	// For adding a new user to all clients' games
	static public class AddUser {
		public User user;
	}

	// For updating the location of a user
	static public class UpdateUser {
		public int id;
		public float x, y;
	}

	// For removing a user from the game when they disconnect
	static public class RemoveUser {
		public int id;
	}
	
	// For client to tell the server its player moved
	static public class MovePlayer {
		public float x, y;
	}
}
