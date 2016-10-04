


	import java.sql.*;
	

	public class DBConnect {
		public static Connection connection;
		public static void main(String[] args) throws SQLException {
			// TODO Auto-generated method stub
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
				//DriverManager.getConnection(url, user, password)
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
			
			InsertUser("Luke","SillyGoose",connection); //add strings here to send them to the user table in the database
				}
		
		public static void InsertUser(String username, String password, Connection connection) throws SQLException{ //inserts a user into the database using the given connection
			if(connection==null){
				System.out.println("No connection found");
				return;
			}
			
			Statement stmt = connection.createStatement() ;
			stmt.executeUpdate("INSERT INTO Users " + "VALUES ('"+username+"', '"+password+"')"); //will throw an exception if there is already a user with the same name in the database
			return;
		}

		}


