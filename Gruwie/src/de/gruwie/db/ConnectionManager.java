package de.gruwie.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import de.gruwie.util.Formatter;

public class ConnectionManager {
	
	private static Connection connection;
	
	public static Connection getConnection () throws Exception {
		if(connection == null) createConnection();
		return connection;
	}
	
	public static boolean createConnection () {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void closeConnection() throws Exception {
		if(connection != null) connection.close();
	}
	
	private static final String[] DEFAULT_TABLES = {"CREATE TABLE output_channel (guildId int(64) primary key, channelId int(64) unique not null)",
													"CREATE TABLE track (iD integer primary key, url varchar unique not null, genre1 int, genre2 int, genre3 int)",
													"CREATE TABLE playlist (iD int(64) not null, isUser boolean not null, playlist_name varchar not null, trackid int not null)",
													"CREATE TABLE played (userId int(64) not null, trackid int, count int DEFAULT 0, PRIMARY KEY (userId, trackId))"};
	public static void initializeDatabase() throws Exception {
		for (int i = 0; i < DEFAULT_TABLES.length; i++) {
			Statement stmt = connection.createStatement();
			stmt.execute(DEFAULT_TABLES[i]);
		}
	}
}
