package de.gruwie.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import de.gruwie.util.Formatter;
import de.gruwie.util.GruwieIO;

public class ConnectionManager {
	
	private static Connection connection;
	
	public static Connection getConnection () throws Exception {
		if(connection == null) createConnection();
		return connection;
	}
	
	public static boolean createConnection () throws Exception {
			
		boolean newFile = false;
			
		File file = new File("data.db");
		if(!file.exists()) {
			System.out.println("No Database found --> creating an empty default-Database");
			Formatter.printBorderline("-");
			file.createNewFile();
			newFile = true;
		}
		String url = "jdbc:sqlite:" + file.getPath();
		if((connection = DriverManager.getConnection(url)) != null) {
			if(newFile) initializeDatabase();
			return true;
		}
		else return false;
			
	}
	
	public static void closeConnection() throws Exception {
		if(connection != null) connection.close();
	}
	
	public static void initializeDatabase() throws Exception {
		List<String> default_tables = GruwieIO.readFromFile("default_tables.txt");
		for (String query : default_tables) {
			Statement stmt = connection.createStatement();
			stmt.execute(query);
		}
	}
}
