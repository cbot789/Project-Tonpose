


	import java.sql.*;
	

	public class DBConnect {
		public static Connection connection;
		public static void main(String[] args) throws SQLException { //TODO remove main method and just use the functions
			try{
				Class.forName("com.mysql.jdbc.Driver");
			}
			catch(ClassNotFoundException e){
				System.out.println("Mysql jdbc Driver error");
				e.printStackTrace();
				return;
			}

			Connection connection=null;

			try{
				connection=DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu:3306/db309dk02","dbu309dk02", "E47EWpMYLCQ");
				//DriverManager.getConnection(url, user, password) this is the format of the get connection method
			}
			catch(SQLException e){
				System.out.println("Connection Failed!");
				e.printStackTrace();
				return;
			}
			if(connection!=null){
				System.out.println("connection successful!");
			}
			else{
				System.out.println("Connection failed!");
			}
			System.out.println(userExists("Luke",connection)); //checks if the username given is in the database
			System.out.println(userExists("Luke","SillyGoose",connection));
			insertUser("Luke","SillyGoose",connection); //add strings here to send them to the user table in the database
				}
		
		public static void insertUser(String username, String password, Connection connection) throws SQLException{ //inserts a user into the database using the given connection
			if(connection==null){
				System.out.println("No connection found");
				return;
			}
			if(userExists(username, connection)){
				System.out.println("username already exists");
				return;
			}
			Statement stmt = connection.createStatement() ;
			stmt.executeUpdate("INSERT INTO Users " + "VALUES ('"+username+"', '"+password+"')"); //will throw an exception if there is already a user with the same name in the database
			return;
		}
		
		public static boolean userExists(String username, Connection connection) throws SQLException{ //will check if a specific user exists in the database. is NOT case sensitive
			Statement stmt=connection.createStatement();
			String queryCheck = "SELECT * from Users WHERE UserName = + '"+username+"'"; //TODO add case sensitivity to the query
			ResultSet rs = stmt.executeQuery(queryCheck);
			if(rs.absolute(1)){   //checks if the result set has a value in its first row. Returns false if the row is empty, ie no matching username exists.
				return true;
			}
			else{
				return false;
			}	
		}
		public static boolean userExists(String username, String password, Connection connection) throws SQLException{ //same as previous method, but also checks if the password matches
			Statement stmt=connection.createStatement();
			String queryCheck = "SELECT * from Users WHERE UserName = + '"+username+"'";
			ResultSet rs = stmt.executeQuery(queryCheck);
		    if(rs.next()){ //will iterate to the given username if it exists
		    if(rs.getString("UserName").equals(username)&&rs.getString("Password").equals(password)){
		    	return true;
		    }
		    }
		    return false;
		}
		
		public static Connection establishConnection() throws SQLException{ //returns a connection to the MySQL database
			Connection connection=DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu:3306/db309dk02","dbu309dk02", "E47EWpMYLCQ");
			return connection;
		}

		}


