package cs309.tonpose;
import java.sql.*;
public class DatabaseCommands {

	
	
	public static boolean insertUser(String username, String password, Connection connection) throws SQLException{ //inserts a user into the database using the given connection
		if(connection==null){
			System.out.println("No connection found");
			return false;
		}
		if(userExists(username, connection)){
			System.out.println("username already exists");
			return false;
		}
		Statement stmt = connection.createStatement() ;
		stmt.executeUpdate("INSERT INTO Users " + "VALUES ('"+username+"', '"+password+"')"); //will throw an exception if there is already a user with the same name in the database
		return true;
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
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e){
			System.out.println("Mysql jdbc Driver error");
			e.printStackTrace();
			return null;
		}

		Connection connection=null;

		try{
			connection=DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu:3306/db309dk02?autoReconnect=true&useSSL=false","dbu309dk02", "E47EWpMYLCQ");
			//DriverManager.getConnection(url, user, password) this is the format of the get connection method
		}
		catch(SQLException e){
			System.out.println("Connection Failed!");
			e.printStackTrace();
			return null;
		}
		connection=DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu:3306/db309dk02?autoReconnect=true&useSSL=false","dbu309dk02", "E47EWpMYLCQ");
		return connection;
	}
	
}
