package sqlutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

	private static String PROPERTIES_FILENAME = "sqlutils.properties";

	protected static Connection getDBConnection() {

		Properties properties = new Properties();
		FileInputStream fis = null;
		Connection dbConnection = null;

		try {
			fis = new FileInputStream(PROPERTIES_FILENAME);
			properties.load(fis);

			Class.forName(properties.getProperty("DB_DRIVER_CLASS"));

			dbConnection = DriverManager.getConnection(
					properties.getProperty("DB_URL"),
					properties.getProperty("DB_USERNAME"),
					properties.getProperty("DB_PASSWORD"));

		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.err.println("There was an error reading "
					+ PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			System.exit(1);
		}
		return dbConnection;
	}

}
