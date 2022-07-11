package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import de.gruwie.db.ConnectionManager;
import de.gruwie.util.GruwieUtilities;

public class TrackDA {

	public static boolean writeTrack(String url) {
		GruwieUtilities.log();
		GruwieUtilities.log("url=" + url);
		if(!trackExists(url)) {
			try (Connection cn = ConnectionManager.getConnection(false)) {
				try (PreparedStatement pstmt = cn.prepareStatement("INSERT INTO track (url) VALUES (?)")){
					pstmt.setString(1, url);
					boolean result = pstmt.executeUpdate() == 1;
					cn.commit();
					return result;
				} catch (Exception e) {
					cn.rollback();
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	private static boolean trackExists(String url) {
		GruwieUtilities.log();
		GruwieUtilities.log("url=" + url);
		try (Connection cn = ConnectionManager.getConnection(true)) {
			try (PreparedStatement pstmt = cn.prepareStatement("SELECT * FROM track WHERE url = ?")){
				pstmt.setString(1, url);
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()) return true;
					else return false;
				}
			} 
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static boolean deleteCertainTrack(String url) {
		GruwieUtilities.log();
		GruwieUtilities.log("url=" + url);
		try (Connection cn = ConnectionManager.getConnection(false)) {
			try {
				deleteFromPlaylists(cn, url);
				deleteFromTracks(cn, url);
				cn.commit();
				return true;
			} catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void deleteFromTracks(Connection cn, String url) throws Exception{
		GruwieUtilities.log();
		GruwieUtilities.log("url=" + url);
		try (PreparedStatement pstmt = cn.prepareStatement("DELETE FROM TRACK WHERE url = ?")){
			pstmt.setString(1, url);
			pstmt.executeUpdate();
		} 
	}
	
	private static void deleteFromPlaylists(Connection cn, String url) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("url=" + url);
		try (PreparedStatement pstmt = cn.prepareStatement("DELETE FROM playlist WHERE trackid IN (SELECT iD FROM track WHERE url = ?)")){
			pstmt.setString(1, url);
			pstmt.executeUpdate();
		} 
	}
	
	public static int getTrackCount() {
		GruwieUtilities.log();
		try(Connection cn = ConnectionManager.getConnection(true)) {
			try(Statement stmt = cn.createStatement()) {
				try(ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM track")) {
					if(rs.next()) return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
