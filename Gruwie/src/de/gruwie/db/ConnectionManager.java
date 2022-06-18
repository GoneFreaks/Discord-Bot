package de.gruwie.db;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteDataSource;

import de.gruwie.db.da.MetaDA;
import de.gruwie.util.GruwieUtilities;

public class ConnectionManager {
	
	private static SQLiteDataSource source;
	
	public static Connection getConnection (boolean autoCommit) throws SQLException {
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
				GruwieUtilities.printBorderline("-");
				file.createNewFile();
				newFile = true;
			}
			source = new SQLiteDataSource();
			source.setUrl("jdbc:sqlite:" + file.getPath());
			if(source.getConnection() != null) {
				if(newFile) initializeDatabase();
				else if(!validateDatabase()) return false;
				return true;
			}
			else return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean validateDatabase() {
		
		try (Connection cn = getConnection(false)) {
			MetaDA meta = new MetaDA(cn);
			try {
				List<String> table_names = meta.getAllTableNames();
				for (int i = 0; i < DEFAULT_TABLES.length; i++) {
					String current = DEFAULT_TABLES[i];
					int begin_index = current.indexOf("TABLE") + 6;
					int end_index = current.indexOf("(");
					String table_name = current.substring(begin_index, end_index).trim();
					table_names.remove(table_name);
					
					List<String> column_names = new ArrayList<>();
					String[] splitted = current.substring(current.indexOf("(")).split(" ");
					for(int j = 0; j < splitted.length; j++) {
						if(splitted[j].contains("unique(")) break;
						if(j > 0) {if(splitted[j-1].contains(",")) column_names.add(splitted[j]);}
						else column_names.add(splitted[j].replace("(", ""));
					}
					
					if(!meta.compareDBToDDL(table_name, column_names)) {
						System.out.println("Table doesn't match the DDL - Trying to convert " + table_name + " -");
						meta.renameTable(table_name);
						meta.createNewTable(current);
						meta.moveData(table_name, column_names.size());
						meta.dropTable(table_name  + "_temp");
					}
				}
				for (String i : table_names) {
					meta.dropTable(i);
				}
				cn.commit();
				return true;
			} catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	private static final String[] DEFAULT_TABLES = {
			"CREATE TABLE output_channel (guildId int(64) primary key, channelId int(64) unique not null)",
			"CREATE TABLE track (iD integer primary key, url varchar unique not null, startpoint int, endpoint int)",
			"CREATE TABLE playlist (iD int(64) not null, isUser boolean not null, playlist_name varchar not null, track int not null, unique(iD, isUser, playlist_name, track))"};
	public static void initializeDatabase() throws Exception {
		try (Connection cn = getConnection(false)){
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
