package cs309.tonpose;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
//import com.esotericsoftware.minlog.Log;

import cs309.tonpose.Network.CheckUser;
import cs309.tonpose.Network.CheckUsername;
import cs309.tonpose.Network.MovePlayer;
import cs309.tonpose.Network.NewUser;
import cs309.tonpose.Network.PlayerConnect;
import cs309.tonpose.Network.PlayerDisconnect;
import cs309.tonpose.Network.UpdatePlayerList;


public class tonpose_server {
	
	Server server;
	java.sql.Connection db_connection;

	public tonpose_server () throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new tonpose_connection();
			}
		};

		//Registers the server and all packet classes
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {

				tonpose_connection connection = (tonpose_connection)c;

				// Search for username in database
				if (object instanceof CheckUsername) {

					// Status object to be sent back to the client
					CheckUsername returnStatus = new CheckUsername();
					
					// Send back 'false' if username is invalid
					String name = ((CheckUsername)object).name;
					if (name == null) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					name = name.trim();
					if (name.length() == 0) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					
					// Connect to database with jdbc
					try {
						db_connection = DatabaseCommands.establishConnection();
					} 
					catch (SQLException e1) {
						e1.printStackTrace();
					}

					// Check if 'name' is a username in the database
					try {
						boolean exists = DatabaseCommands.userExists(name, db_connection);
						returnStatus.status = exists;
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					// Send result of database search back to client
					server.sendToTCP(connection.getID(), returnStatus);
					return;
				}
				
				// Search for username and password combo in database
				if (object instanceof CheckUser) {

					// Status object to be sent back to the client
					CheckUser returnStatus = new CheckUser();
					
					// Send back 'false' if username or password is invalid
					String name = ((CheckUser)object).name;
					String password = ((CheckUser)object).password;
					if (name == null || password == null) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					name = name.trim();
					password = password.trim();
					if (name.length() == 0 || password.length() == 0) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					
					// Connect to database with jdbc
					try {
						db_connection = DatabaseCommands.establishConnection();
					} 
					catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					// Check if name and password combo is in the database
					try {
						boolean exists = DatabaseCommands.userExists(name, password, db_connection);
						returnStatus.status = exists;
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					// Send result of database search back to client
					server.sendToTCP(connection.getID(), returnStatus);
					return;
				}
				
				// Add a new user to the database
				if (object instanceof NewUser) {

					// Status object to be sent back to the client
					NewUser returnStatus = new NewUser();
					
					// Send back 'false' if username or password is invalid
					String name = ((NewUser)object).name;
					String password = ((NewUser)object).password;
					if (name == null || password == null) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					name = name.trim();
					password = password.trim();
					if (name.length() == 0 || password.length() == 0) {
						returnStatus.status = false;
						server.sendToTCP(connection.getID(), returnStatus);
						return;
					}
					
					// Connect to database with jdbc
					try {
						db_connection = DatabaseCommands.establishConnection();
					} 
					catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					// Add new user to database
					try {
						boolean success = DatabaseCommands.insertUser(name, password, db_connection);
						returnStatus.status = success;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					//send back whether user was successfully added to database
					server.sendToTCP(connection.getID(), returnStatus);
					return;
				}
				
				if (object instanceof MovePlayer) {

					
				}
				
			}

			public void disconnected (Connection c) {
				//tonpose_connection connection = (tonpose_connection)c;
				updatePlayers();
			}
		});
		server.bind(Network.port);
		server.start();

		System.out.println("Server started on port " + Network.port);
	}

	void updatePlayers () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList<String> names = new ArrayList<String>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			tonpose_connection connection = (tonpose_connection)connections[i];
			names.add(connection.name);
		}
		UpdatePlayerList updatePlayers = new UpdatePlayerList();
		updatePlayers.names = (String[])names.toArray(new String[names.size()]);
		//server.sendToAllTCP(updatePlayers);
	}

public static void closeConnection(Connection conn){
	if (conn!=null){
		try{
			conn.close();
			}
catch(SQLException e){

			}
		}

}


	//holds connection state
	static class tonpose_connection extends Connection {
		public String name;
	}

	public static void main (String[] args) throws IOException {
		//Log.set(Log.LEVEL_DEBUG);
		new tonpose_server();
	}
}
