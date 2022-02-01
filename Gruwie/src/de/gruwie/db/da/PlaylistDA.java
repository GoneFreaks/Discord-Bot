package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.gruwie.db.ConnectionManager;
import de.gruwie.util.ConfigManager;

public class PlaylistDA {
	
	public static List<String> readAllPlaylists(long iD, boolean isUser) {
		List<String> playlists = new ArrayList<>();
		
		String query = "SELECT DISTINCT playlist_name FROM playlist WHERE iD = ? AND isUser = ?"; 
		try (Connection cn = ConnectionManager.getConnection(true)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				pstmt.setLong(1, iD);
				pstmt.setBoolean(2, isUser);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						playlists.add(rs.getString(1));
					}
					return playlists;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static boolean playlistExists(long iD, boolean isUser, String playlist_name) {
		
		try (Connection cn = ConnectionManager.getConnection(true)){
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT playlist_name FROM playlist WHERE playlist_name = ? AND isUser = ? AND iD = ?")){
				pstmt.setString(1, playlist_name);
				pstmt.setBoolean(2, isUser);
				pstmt.setLong(3, iD);
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()) return true;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean writePlaylist(List<String> tracks, String name, long iD, boolean isUser, boolean update) {
		
		if(!update && playlistExists(iD, isUser, name)) return false;
		
		List<Integer> track_ids = insertIntoTracks(tracks);
		
		String query = "INSERT INTO playlist (iD, isUser, playlist_name, track) VALUES (?,?,?,?)";
		
		try (Connection cn = ConnectionManager.getConnection(false)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				for (Integer i : track_ids) {
					pstmt.setLong(1, iD);
					pstmt.setBoolean(2, isUser);
					pstmt.setString(3, name);
					pstmt.setInt(4, i);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				cn.commit();
				return true;
			}
			catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static List<String> readPlaylist(String name, long id, boolean isUser) {
		List<String> urls = new ArrayList<>();
		
		String query = "SELECT url FROM track t JOIN playlist p ON t.iD = p.track WHERE p.id = ? AND isUser = ? AND playlist_name = ? ORDER BY t.iD";
		try (Connection cn = ConnectionManager.getConnection(true)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				pstmt.setLong(1, id);
				pstmt.setBoolean(2, isUser);
				pstmt.setString(3, name);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						urls.add(rs.getString(1));
					}
					return urls;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<Integer> insertIntoTracks(List<String> playlist_complete) {
		List<Integer> result = new ArrayList<>();
		
		try (Connection cn = ConnectionManager.getConnection(false)) {
			try(PreparedStatement pstmt = cn.prepareStatement("INSERT OR IGNORE INTO TRACK (url) VALUES (?)")){
				
				for (String i : playlist_complete) {
					pstmt.setString(1, i);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				
				result.addAll(getTrackIds(playlist_complete));
				cn.commit();
			}catch(Exception ex) {
				cn.rollback();
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static List<Integer> getTrackIds(List<String> urls) throws Exception{
		
		List<Integer> available_id = new ArrayList<>();
		
		try (Connection cn = ConnectionManager.getConnection(true)) {
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT iD FROM track WHERE url = ?")){
				for (String i : urls) {
					pstmt.setString(1, i);
					try(ResultSet rs = pstmt.executeQuery()) {
						while(rs.next()) {
							available_id.add(rs.getInt(1));
						}
					}
				}
			}
			return available_id;
		} 
	}
	
	public static List<String> readRandom (int count) {
		List<String> urls = new ArrayList<>();
		
		try (Connection cn = ConnectionManager.getConnection(true)) {
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT url FROM track ORDER BY RANDOM() LIMIT ?")){
				pstmt.setInt(1, count);
				try(ResultSet rs = pstmt.executeQuery()) {
					while(rs.next()) urls.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}
	
	public static boolean deletePlaylist(long id, boolean isUser, String name) {
		try (Connection cn = ConnectionManager.getConnection(false)) {
			try(PreparedStatement pstmt = cn.prepareStatement("DELETE FROM playlist WHERE iD = ? AND isUser = ? AND playlist_name = ?")){
				pstmt.setLong(1, id);
				pstmt.setBoolean(2, isUser);
				pstmt.setString(3, name);
				int result = pstmt.executeUpdate();
				cn.commit();
				return result > 0;
			} catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updatePlaylist(long id, boolean isUser, String name, List<String> urls) {
		List<String> already = readPlaylist(name, id, isUser);
		if(already.size() < 1) return false;
		for (String i : urls) {
			if(!already.contains(i)) already.add(i);
		}
		if(already.size() > ConfigManager.getInteger("max_queue_size")) return false;
		return writePlaylist(already, name, id, isUser, true);
	}
	
}
