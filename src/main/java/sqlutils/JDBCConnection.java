package sqlutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	 static final String USER = "old_db";
	 static final String PASS = "111"; 
	 
	 protected static Connection getDBConnection() {
		    Connection dbConnection = null;
		    try {
		        Class.forName(JDBC_DRIVER);
		    } catch (ClassNotFoundException e) {
		        System.out.println(e.getMessage());
		    }
		    try {
		        dbConnection = DriverManager.getConnection(DB_URL, USER,PASS);
		    } catch (SQLException e) {
		        System.out.println(e.getMessage());
		    }
		    return dbConnection;
		}
	
}
