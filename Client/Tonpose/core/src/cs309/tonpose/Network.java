package cs309.tonpose;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

//Core game packet classes
public class Network {
	static public final int port = 8080;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String[].class);
		kryo.register(CheckUsername.class);
		kryo.register(CheckUser.class);
		kryo.register(NewUser.class);
		kryo.register(PlayerConnect.class);
		kryo.register(UpdatePlayerList.class);
		kryo.register(PlayerDisconnect.class);
		kryo.register(MovePlayer.class);
	}
	static public class CheckUsername {
		public String name;
		public boolean status;
	}

	static public class CheckUser {
		public String name, password;
		public boolean status;
	}

	static public class NewUser {
		public String name, password;
		public boolean status;
	}

	static public class PlayerConnect {
		public String name;
	}

	static public class UpdatePlayerList {
		public String[] names;
	}

	static public class PlayerDisconnect {
		public String name;
	}

	static public class MovePlayer {
		public String name;
		public int x, y;
	}
}