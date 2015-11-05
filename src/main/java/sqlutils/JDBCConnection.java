package sqlutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	 static final String USER = "jdbc_study";
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
		        return dbConnection;
		    } catch (SQLException e) {
		        System.out.println(e.getMessage());
		    }
		    return dbConnection;
		}
	
}
