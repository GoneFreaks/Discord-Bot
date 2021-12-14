package de.gruwie.db.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.gruwie.db.ConnectionManager;

public class TrackDA {

	public static boolean writeTrack(String url) {
		if(!trackExists(url)) {
			try (PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("INSERT INTO track (url) VALUES (?)")){
				pstmt.setString(1, url);
				return pstmt.executeUpdate() == 1;
			} catch (Exception e) {
				return false;
			}
		}
		else return false;
	}
	
	private static boolean trackExists(String url) {
		try (PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("SELECT * FROM track WHERE url = ?")){
			pstmt.setString(1, url);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) return true;
				else return false;
			}
		} catch (Exception e) {
			return false;
		} 
	}
	
	public static boolean deleteCertainTrack(String url) {
		return true;
	}
	
}
