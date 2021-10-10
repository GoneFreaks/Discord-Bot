package de.gruwie.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.gruwie.ConfigManager;

public class GetDataBaseConnection {

	private static final String SERVER = "";
	private static final int PORT = -1;
	private static final String DATABASENAME = "";
	private static final String USERNAME = "";
	private static final String PASSWORD = "";
	
	private static MysqlDataSource datasource;
	
	public static Connection getConnection () throws SQLException {
		return datasource.getConnection();
	}
	
	public static boolean createConnection () throws SQLException {
		
		if(ConfigManager.getBoolean("database")) {
			
			datasource = new MysqlDataSource();
			datasource.setServerName(SERVER);
			datasource.setPort(PORT);
			datasource.setDatabaseName(DATABASENAME);
			datasource.setUser(USERNAME);
			datasource.setPassword(PASSWORD);
			
			try(Connection cn = getConnection()){
				if(cn != null) return true;
				else return false;
			}
		}
		else return true;
	}
	
}
