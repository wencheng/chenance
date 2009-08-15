

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDateTime {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String jdbcUrl = "jdbc:sqlite:/Users/wencheng/Desktop/dt.db";

		try {
			Connection conn = DriverManager.getConnection(jdbcUrl);
			Statement stmt = conn.createStatement();
//			stmt.execute("create table t ( " +
//					" id INTEGER PRIMARY KEY AUTOINCREMENT" +
//					",dt TIMESTAMP" +
//					",dt_d TIMESTAMP default current_timestamp" +
//					")");
//			stmt.execute("insert into t (dt) values (1250262000)");
//			stmt.execute("insert into t (dt) values ('2009-08-14 00:00:00')");
			ResultSet rs = stmt.executeQuery("select id, dt, dt_d from t");
			System.err.println(rs.getMetaData().getColumnTypeName(1));
			System.err.println(rs.getMetaData().getColumnTypeName(2));
			System.err.println(rs.getMetaData().getColumnTypeName(3));
			
			while ( rs.next() ) {
				
				System.out.print(rs.getLong(1));
				System.out.print("    ");
				System.out.print(rs.getLong(2));
				System.out.print("    ");
				System.out.print(rs.getTime(2));
				System.out.print("    ");
				System.out.print(rs.getString(3));
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
