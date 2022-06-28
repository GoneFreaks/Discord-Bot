package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.gruwie.db.ConnectionManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.dto.TrackDTO;
import de.gruwie.util.exceptions.PlaylistAlreadyExistsException;
import de.gruwie.util.exceptions.TooManyPlaylistsException;

public class PlaylistDA {
	
	public static List<String> readAllPlaylists(long iD, boolean isUser) {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser);
		List<String> playlists = new ArrayList<>();
		
		String query = "SELECT DISTINCT playlist_name FROM playlist WHERE iD = ? AND isUser = ?"; 
		try (Connection cn = ConnectionManager.getConnection(true)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				pstmt.setLong(1, iD);
				pstmt.setBoolean(2, isUser);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) playlists.add(rs.getString(1));
					GruwieUtilities.log(playlists.size() + " " + playlists.toString());
					return playlists;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static boolean playlistExists(long iD, boolean isUser, String playlist_name) {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser + " playlist_name=" + playlist_name);
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
	
	public static boolean writePlaylist(List<String> tracks, String playlist_name, long iD, boolean isUser, boolean update) throws TooManyPlaylistsException, PlaylistAlreadyExistsException {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser + " update=" + update + " tracks_size=" + tracks.size() + " playlist_name=" + playlist_name);
		countSmallerMax(iD, isUser);
		
		if(!update && playlistExists(iD, isUser, playlist_name)) throw new PlaylistAlreadyExistsException(playlist_name);
		
		List<Integer> track_ids = insertIntoTracks(tracks);
		
		String query = "INSERT INTO playlist (iD, isUser, playlist_name, track) VALUES (?,?,?,?)";
		
		try (Connection cn = ConnectionManager.getConnection(false)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				for (Integer i : track_ids) {
					pstmt.setLong(1, iD);
					pstmt.setBoolean(2, isUser);
					pstmt.setString(3, playlist_name);
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
	
	private static boolean countSmallerMax(long iD, boolean isUser) throws TooManyPlaylistsException {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser);
		try(Connection cn = ConnectionManager.getConnection(true)) {
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT COUNT(*) AS C FROM (SELECT DISTINCT playlist_name FROM playlist WHERE iD = ?)")) {
				pstmt.setLong(1, iD);
				try(ResultSet rs = pstmt.executeQuery()) {
					if(rs.next()) {
						long result = rs.getLong(1);
						if(result < (isUser? 4 : 20)) return true;
						else throw new TooManyPlaylistsException("Current Count: " + (result + 1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}

	public static List<TrackDTO> readPlaylist(String playlist_name, long iD, boolean isUser) {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser + " playlist_name=" + playlist_name);
		List<TrackDTO> tracks = new ArrayList<>();
		
		String query = "SELECT url, startpoint, endpoint FROM track t JOIN playlist p ON t.iD = p.track WHERE p.id = ? AND isUser = ? AND playlist_name = ? ORDER BY t.iD";
		try (Connection cn = ConnectionManager.getConnection(true)){
			try(PreparedStatement pstmt = cn.prepareStatement(query)){
				pstmt.setLong(1, iD);
				pstmt.setBoolean(2, isUser);
				pstmt.setString(3, playlist_name);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) tracks.add(new TrackDTO(rs.getString(1), rs.getLong(2), rs.getLong(3)));
					GruwieUtilities.log(tracks.size() + " " + tracks.toString());
					return tracks;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<Integer> insertIntoTracks(List<String> playlist_complete) {
		GruwieUtilities.log();
		GruwieUtilities.log("playlist_size=" + playlist_complete.size());
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
		GruwieUtilities.log(result.size() + " " + result.toString());
		return result;
	}
	
	private static List<Integer> getTrackIds(List<String> urls) throws Exception{
		GruwieUtilities.log();
		GruwieUtilities.log("urls_count=" + urls.size());
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
			GruwieUtilities.log(available_id.size() + " " + available_id.toString());
			return available_id;
		} 
	}
	
	public static List<TrackDTO> readRandom (int count) {
		GruwieUtilities.log();
		GruwieUtilities.log("random_count=" + count);
		List<TrackDTO> tracks = new ArrayList<>();
		
		try (Connection cn = ConnectionManager.getConnection(true)) {
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT url, startpoint, endpoint FROM track ORDER BY RANDOM() LIMIT ?")){
				pstmt.setInt(1, count);
				try(ResultSet rs = pstmt.executeQuery()) {
					while(rs.next()) tracks.add(new TrackDTO(rs.getString(1), rs.getLong(2), rs.getLong(3)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GruwieUtilities.log(tracks.size() + " " + tracks.toString());
		return tracks;
	}
	
	public static boolean deletePlaylist(long iD, boolean isUser, String playlist_name) {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser + " playlist_name=" + playlist_name);
		try (Connection cn = ConnectionManager.getConnection(false)) {
			try(PreparedStatement pstmt = cn.prepareStatement("DELETE FROM playlist WHERE iD = ? AND isUser = ? AND playlist_name = ?")){
				pstmt.setLong(1, iD);
				pstmt.setBoolean(2, isUser);
				pstmt.setString(3, playlist_name);
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
	
	public static boolean updatePlaylist(long iD, boolean isUser, String playlist_name, List<String> urls) {
		GruwieUtilities.log();
		GruwieUtilities.log("iD=" + iD + " isUser=" + isUser + " playlist_name=" + playlist_name + " urls_count=" + urls.size());
		if(deletePlaylist(iD, isUser, playlist_name)) {
			if(urls.size() == 0) return true;
			if(urls.size() > ConfigManager.getInteger("max_queue_size")) return false;
			try {
				return writePlaylist(urls, playlist_name, iD, isUser, true);
			} catch (TooManyPlaylistsException e) {
				e.printStackTrace();
				return false;
			} catch (PlaylistAlreadyExistsException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public static int getPlaylistCount (boolean isUser) {
		GruwieUtilities.log();
		GruwieUtilities.log("isUser=" + isUser);
		try(Connection cn = ConnectionManager.getConnection(true)) {
			try(PreparedStatement pstmt = cn.prepareStatement("SELECT COUNT(*) AS C FROM (SELECT DISTINCT playlist_name FROM playlist WHERE isUser = ?)")) {
				pstmt.setBoolean(1, isUser);
				try(ResultSet rs = pstmt.executeQuery()) {
					if(rs.next()) return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
