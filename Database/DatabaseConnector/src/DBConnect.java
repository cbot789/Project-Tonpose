


	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	

	public class DBConnect {
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
			/*Statement stmt = connection.createStatement() ;
			String query = "select columnname from tablename ;" ;
			ResultSet rs = stmt.executeQuery(query) ;*/

				}

		}


