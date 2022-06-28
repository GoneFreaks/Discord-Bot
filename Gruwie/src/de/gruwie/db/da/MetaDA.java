package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import de.gruwie.util.GruwieUtilities;

public class MetaDA {

	private Connection cn;
	
	public MetaDA (Connection cn) {
		this.cn = cn;
	}
	
	public boolean compareDBToDDL(String table_name, List<String> expected_column_names) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("table_name=" + table_name + " expected_column_names_size=" + expected_column_names.size());
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
		return true;
	}
	
	public void renameTable(String table_name) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("table_name=" + table_name);
		try (Statement stmt = cn.createStatement()){
			stmt.executeUpdate("ALTER TABLE " + table_name + " RENAME TO " + table_name + "_temp");
		} 
	}

	public void createNewTable(String current_ddl) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("current_ddl=" + current_ddl);
		try (Statement stmt = cn.createStatement()){
			stmt.executeUpdate(current_ddl);
		} 
	}
	
	public void moveData(String table_name, int column_count) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("table_name=" + table_name + " column_count=" + column_count);
		try (Statement stmt = cn.createStatement()){
			String front = "INSERT INTO " + table_name + " SELECT ";
			String back = " FROM " + table_name + "_temp";
			String select = getSelectClause(table_name, column_count);
			stmt.executeUpdate(front + select + back);
		} 
	}
	
	private String getSelectClause(String table_name, int column_count) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("table_name=" + table_name + " column_count=" + column_count);
		String sql = "SELECT name from PRAGMA_table_info(?) WHERE name IN (SELECT name from PRAGMA_table_info(?))";
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
				GruwieUtilities.log(b.toString());
				return b.toString();
			}
		}  
	}

	public void dropTable(String table_name) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("table_name=" + table_name);
		try (Statement stmt = cn.createStatement()) {
			stmt.executeUpdate("DROP TABLE " + table_name);
		}
	}
	
	public List<String> getAllTableNames() throws Exception {
		GruwieUtilities.log();
		List<String> result = new LinkedList<>();
		try (Statement stmt = cn.createStatement()) {
			try (ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_schema WHERE type='table'")) {
				while(rs.next()) result.add(rs.getString("name"));
				GruwieUtilities.log(result.toString());
				return result;
			}
		}
	}
	
}
