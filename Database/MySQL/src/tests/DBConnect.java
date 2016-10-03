package tests;
import java.sql.*;
import java.sql.DriverManager;;
public class DBConnect {

/* NOTES
 * 	• While connecting to remote database(university server)
	connection = DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu:port/db309grp23","dbu309grp23", "ZHaMv5K6B5c");
	
	• While connecting to local database
	connection = DriverManager.getConnection("jdbc:mysql://localhost:port/schema","root","password")*/
	
	public static void main(String[] args) {
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
	connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","matilda2006"); //this is a local database, code will need to change for university db
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


	}

}
