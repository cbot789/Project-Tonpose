package cs309.tonpose;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import cs309.tonpose.Network.CheckUser;
import cs309.tonpose.Network.CheckUsername;
import cs309.tonpose.Network.NewUser;
import cs309.tonpose.Network.ClientConnect;
import cs309.tonpose.Network.AddUser;
import cs309.tonpose.Network.UpdateUser;
import cs309.tonpose.Network.RemoveUser;
import cs309.tonpose.Network.MovePlayer;


public class tonpose_server {
	
	Server server;
	java.sql.Connection db_connection;
	HashSet<User> users = new HashSet();

	public tonpose_server () throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				return new tonpose_connection();
			}
		};

		//Registers the server and all packet classes
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {

				tonpose_connection connection = (tonpose_connection)c;
				User user = connection.user;

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
					try {
						DatabaseCommands.userExists(name, db_connection);
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
					try {
						DatabaseCommands.userExists(name, db_connection);
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
					//check if username or password are empty
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
					try {
						DatabaseCommands.userExists(name, db_connection);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return;
				}
				if (object instanceof ClientConnect) {
					if (user != null) return;	//ignore if user already exists
					ClientConnect connect = (ClientConnect)object;
					
					//create new user object
					user = new User();
					user.name = connect.name;
					user.id = connection.getID();
					user.x = connect.x;
					user.y = connect.y;
					
					//set the connection state user to the new user object
					connection.user = user;
					
					//send all current users to the new user
					for (User other : users) {
						AddUser add = new AddUser();
						add.user = other;
						connection.sendTCP(add);
					}
					//add new user to the hashmap
					users.add(user);
					AddUser add = new AddUser();
					add.user = user;
					//send new user to all existing users
					server.sendToAllTCP(add);
				}
				if (object instanceof MovePlayer) {
					if (user == null) return;	//if user doesn't exist, ignore
					MovePlayer move = (MovePlayer)object;
					
					//set the current user coordinates to the new move coordinates
					user.x = move.x;
					user.y = move.y;
					
					//create updateUser object to update the user on all clients
					UpdateUser update = new UpdateUser();
					update.id = user.id;
					update.x = user.x;
					update.y = user.y;
					server.sendToAllTCP(update);
				}
			}

			public void disconnected (Connection c) {
				tonpose_connection connection = (tonpose_connection)c;
				if (connection.user != null) {
					//remove the current user from the hashmap
					users.remove(connection.user);
					
					//send a removeUser object to all clients
					RemoveUser r = new RemoveUser();
					r.id = connection.user.id;
					server.sendToAllTCP(r);
				}
			}
		});
		server.bind(Network.port);
		server.start();

		System.out.println("Tonpose Server started on port " + Network.port);
	}

	//holds connection state with user object
	static class tonpose_connection extends Connection {
		public User user;
	}

	public static void main (String[] args) throws IOException {
		//Log.set(Log.LEVEL_DEBUG);
		new tonpose_server();
	}
}
