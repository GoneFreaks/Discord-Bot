package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import de.gruwie.db.ConnectionManager;

public class MetaDA {

	public static boolean compareDBToDDL(String table_name, List<String> expected_column_names) throws Exception {
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()){
				try(ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + table_name + ")")) {
					int counter = 0;
					while(rs.next()) {
						if(!expected_column_names.contains(rs.getString("name"))) return false;
						counter++;
					}
					if(counter != expected_column_names.size()) return false;
				}
			} 
		} 
		return true;
	}
	
	public static void renameTable(String table_name) throws Exception {
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()){
				stmt.executeUpdate("ALTER TABLE " + table_name + " RENAME TO " + table_name + "_temp");
			} 
		} 
	}

	public static void createNewTable(String current_ddl) throws Exception {
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()){
				stmt.executeUpdate(current_ddl);
			} 
		} 
	}
	
	public static void moveData(String table_name, int column_count) throws Exception {
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()){
				String front = "INSERT INTO " + table_name + " SELECT ";
				String back = " FROM " + table_name + "_temp";
				String select = getSelectClause(table_name, column_count);
				stmt.executeUpdate(front + select + back);
			} 
		} 
	}
	
	private static String getSelectClause(String table_name, int column_count) throws Exception {
		String sql = "SELECT name from PRAGMA_table_info(?) WHERE name IN (SELECT name from PRAGMA_table_info(?))";
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (PreparedStatement pstmt = cn.prepareStatement(sql)) {
				pstmt.setString(1, table_name);
				pstmt.setString(2, table_name + "_temp");
				try(ResultSet rs = pstmt.executeQuery()) {
					StringBuilder b = new StringBuilder("");
					int counter = 0;
					while(rs.next()) {
						if(b.length() > 0) b.append(",");
						b.append(rs.getString("name"));
						counter++;
					}
					for (int i = counter; i < column_count; i++) {
						b.append(",null");
					}
					return b.toString();
				}
			} 
		} 
	}

	public static void dropTable(String table_name) throws Exception {
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()) {
				stmt.executeUpdate("DROP TABLE " + table_name);
			}
		}
	}
	
	public static List<String> getAllTableNames() throws Exception {
		List<String> result = new LinkedList<>();
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()) {
				try (ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_schema WHERE type='table'")) {
					while(rs.next()) result.add(rs.getString("name"));
					return result;
				}
			}
		}
	}
	
}
