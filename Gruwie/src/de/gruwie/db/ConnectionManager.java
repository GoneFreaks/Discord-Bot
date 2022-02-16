package de.gruwie.db;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteDataSource;

import de.gruwie.util.Formatter;

public class ConnectionManager {
	
	private static SQLiteDataSource source;
	
	public static Connection getConnection (boolean autoCommit) throws SQLException {
		if(source == null) createConnection();
		Connection cn = source.getConnection();
		cn.setAutoCommit(autoCommit);
		return cn;
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
			source = new SQLiteDataSource();
			source.setUrl("jdbc:sqlite:" + file.getPath());
			if(source.getConnection() != null) {
				if(newFile) initializeDatabase();
				return true;
			}
			else return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static final String[] DEFAULT_TABLES = {"CREATE TABLE output_channel (guildId int(64) primary key, channelId int(64) unique not null)",
			"CREATE TABLE track (iD integer primary key, url varchar unique not null)",
			"CREATE TABLE playlist (iD int(64) not null, isUser boolean not null, playlist_name varchar not null, trackid int not null, unique(iD, isUser, playlist_name, trackid))"};
	public static void initializeDatabase() throws Exception {
		try (Connection cn = source.getConnection()){
			try {
				for (int i = 0; i < DEFAULT_TABLES.length; i++) {
					Statement stmt = cn.createStatement();
					stmt.execute(DEFAULT_TABLES[i]);
				}
				cn.commit();
			} catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
