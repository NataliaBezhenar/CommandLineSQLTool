package sqlutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SQLOperations {

	private final static Logger LOGGER = Logger.getLogger(SQLOperations.class);

	static Pattern pCreateTable = Pattern.compile("\\s*create\\s*table\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pAlterTable = Pattern.compile("\\s*alter\\s*table\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pDropTable = Pattern.compile("\\s*drop\\s*table\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pTruncateTable = Pattern.compile("\\s*truncate\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pInsertInto = Pattern.compile("\\s*insert\\s*into\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pSelect = Pattern.compile("\\s*select\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pCommit = Pattern.compile("\\s*commit\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pRollback = Pattern.compile("\\s*rollback\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pDelete = Pattern.compile("\\s*delete\\s*",
			Pattern.CASE_INSENSITIVE);
	static Pattern pUpdate = Pattern.compile("\\s*update\\s*",
			Pattern.CASE_INSENSITIVE);

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
				LOGGER.info("select statement: " + SQLquery);

				ResultSet rs = statement.executeQuery(SQLquery);

				if (!rs.isBeforeFirst()) {
					LOGGER.info("No rows selected");
					System.out.println("No rows selected");
				}
				ResultSetMetaData rsmd = rs.getMetaData();

				List<String[]> rows = new LinkedList<String[]>();

				String[] columns = new String[rsmd.getColumnCount()];
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					columns[j - 1] = rsmd.getColumnName(j);
				}

				rows.add(columns);
				int rowsCounter=0;
				while (rs.next()) {

					String[] temp = new String[rsmd.getColumnCount()];

					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						temp[i - 1] = rs.getString(i);

					}
					rowsCounter++;
					rows.add(temp);
				}

				TablePrint toCons = new TablePrint();
				toCons.rows = rows;
				System.out.println("\n" + toCons.toString()+"\n"+ rowsCounter+" rows selected");
				LOGGER.info("\n" + toCons.toString()+"\n"+ rowsCounter+" rows selected");

			}

			statement.execute(SQLquery);

			if (m1.find()) {
				LOGGER.info("Table created: " + SQLquery);
				System.out.println("Table created");
			}
			if (m2.find()) {
				LOGGER.info("Table altered: " + SQLquery);
				System.out.println("Alter table done");
			}
			if (m3.find()) {
				LOGGER.info("Table dropped: " + SQLquery);
				System.out.println("Table dropped");
			}
			if (m4.find()) {
				LOGGER.info("Table truncated: " + SQLquery);
				System.out.println("Table truncated");
			}
			if (m5.find()) {
				LOGGER.info("Insert complete: " + SQLquery);
				System.out.println("Insert complete");
			}
			if (m7.find()) {
				LOGGER.info("Commit complete: " + SQLquery);
				System.out.println("Commit complete");
			}
			if (m8.find()) {
				LOGGER.info("Rollback complete: " + SQLquery);
				System.out.println("Rollback complete");
			}
			if (m9.find()) {
				LOGGER.info("Delete complete: " + SQLquery);
				System.out.println("Delete complete");
			}
			if (m10.find()) {
				LOGGER.info("Update complete: " + SQLquery);
				System.out.println("Update complete");
			}

		} catch (SQLException e) {
			LOGGER.error("Error occur in executeUserQuery method: " + e);
			System.out.println("Error occur in executeUserQuery method: " + e);
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

		LOGGER.info("SQLUTILS log");
		LOGGER.warn("Can throw SQLException");
		LOGGER.info("Type \"exit\" to exit the program");
		
		System.out.println("Welcome to SQLUTILS program.\n"
				+ "Enter SQL query or type \"exit\" to exit the program");
		
		Scanner scan = new Scanner(System.in);
		String query = " ";
		while (true) {
			query = scan.nextLine();
			if (query.equals("exit")) {
				scan.close();
				LOGGER.info("End of work");
				System.out.println("End of work. Good bye");
				break;
			}
			executeUserQuery(query);
		}
	}
}
