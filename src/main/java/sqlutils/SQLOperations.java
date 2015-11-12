package sqlutils;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLOperations {
	
	private final static Logger LOGGER = Logger.getLogger(SQLOperations.class
			.getName());

	static Pattern pCreateTable = Pattern.compile("\\s*create\\s*table\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pAlterTable = Pattern.compile("\\s*alter\\s*table\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pDropTable = Pattern.compile("\\s*drop\\s*table\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pTruncateTable = Pattern.compile("\\s*truncate\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pInsertInto = Pattern.compile("\\s*insert\\s*into\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pSelect = Pattern.compile("\\s*select\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pCommit = Pattern.compile("\\s*commit\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pRollback = Pattern.compile("\\s*rollback\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pDelete = Pattern.compile("\\s*delete\\s*",Pattern.CASE_INSENSITIVE);
	static Pattern pUpdate = Pattern.compile("\\s*update\\s*",Pattern.CASE_INSENSITIVE);

	protected static void executeUserQuery(String SQLquery) throws SQLException {
		Connection dbConnection = null;
		Statement statement = null;

		Matcher m1 = pCreateTable.matcher(SQLquery);
		Matcher m2 = pAlterTable.matcher(SQLquery);
		Matcher m3 = pDropTable.matcher(SQLquery);
		Matcher m4 = pTruncateTable.matcher(SQLquery);
		Matcher m5 = pInsertInto.matcher(SQLquery);
		Matcher m6 = pSelect.matcher(SQLquery);
		Matcher m7 = pCommit.matcher(SQLquery);
		Matcher m8 = pRollback.matcher(SQLquery);
		Matcher m9 = pDelete.matcher(SQLquery);
		Matcher m10 = pUpdate.matcher(SQLquery);

		try {
			dbConnection = JDBCConnection.getDBConnection();
			statement = dbConnection.createStatement();

			if (m6.find()) {

				LOGGER.log(Level.INFO, "select statement: " + SQLquery);

				ResultSet rs = statement.executeQuery(SQLquery);

				if (!rs.isBeforeFirst()) {

					LOGGER.log(Level.INFO, "No rows selected");
				}

				while (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();

					for (int i = 1; i <= rsmd.getColumnCount(); i++) {

						if (i > 1) {
							System.out.print("\t");
						}
						System.out.print(rs.getString(i));
					}
					System.out.println();

				}
			}

			statement.execute(SQLquery);

			if (m1.find()) {
				LOGGER.log(Level.INFO, "Create table done: " + SQLquery);
			}
			if (m2.find()) {
				LOGGER.log(Level.INFO, "Alter table done: " + SQLquery);
			}
			if (m3.find()) {
				LOGGER.log(Level.INFO, "Drop table done: " + SQLquery);
			}
			if (m4.find()) {
				LOGGER.log(Level.INFO, "Table truncated: " + SQLquery);
			}
			if (m5.find()) {
				LOGGER.log(Level.INFO, "Insert complete: " + SQLquery);
			}
			if (m7.find()) {
				LOGGER.log(Level.INFO, "Commit complete: " + SQLquery);
			}
			if (m8.find()) {
				LOGGER.log(Level.INFO, "Rollback complete: " + SQLquery);
			}
			if (m9.find()) {
				LOGGER.log(Level.INFO, "Delete complete: " + SQLquery);
			}
			if (m10.find()) {
				LOGGER.log(Level.INFO, "Update complete: " + SQLquery);
			}

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error occur in executeUserQuery method.");
		} finally {
			if (statement != null) {
				statement.close();

			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		
		FileHandler fh;
		String path = Paths.get("").toAbsolutePath().toString();
		try {
			fh = new FileHandler(path + "/LogFile.log");
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			LOGGER.fine("SQLUTILS log");
			LOGGER.warning("Can throw SQLException");
		} catch (SecurityException e) {
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", e);
		}

		LOGGER.log(Level.INFO, "Type \"exit\" to exit the program");
		Scanner scan = new Scanner(System.in);
		String query = " ";
		while (true) {
			query = scan.nextLine();
			if (query.equals("exit")) {
				scan.close();
				LOGGER.log(Level.INFO, "End of work");
				break;
			}
			executeUserQuery(query);
		}
	}
}
