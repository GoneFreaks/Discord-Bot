package de.gruwie.db.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.ConnectionManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.dto.InsertTrackDTO;

public class PlaylistDA {
	
	public static List<String> readAllPlaylists(long iD, boolean isUser) {
		List<String> playlists = new ArrayList<>();
		
		String query = "SELECT DISTINCT playlist_name FROM playlist WHERE iD = ? AND isUser = ?"; 
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(query)){
			pstmt.setLong(1, iD);
			pstmt.setBoolean(2, isUser);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					playlists.add(rs.getString(1));
				}
				return playlists;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean playlistsExists(long iD, boolean isUser, String playlist_name) {
		
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("SELECT playlist_name FROM playlist WHERE playlist_name = ? AND isUser = ? AND iD = ?")){
			pstmt.setString(1, playlist_name);
			pstmt.setBoolean(2, isUser);
			pstmt.setLong(3, iD);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean writePlaylist(List<AudioTrack> tracks, String name, long iD, boolean isUser) {
		
		if(playlistsExists(iD, isUser, name)) return false;
		
		List<String> playlist_complete = new ArrayList<>();
		for (AudioTrack i : tracks) {
			playlist_complete.add(i.getInfo().uri);
		}
		
		List<Integer> track_ids = insertIntoTracks(playlist_complete);
		
		String query = "INSERT INTO playlist (iD, isUser, playlist_name, track) VALUES (?,?,?,?)";
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(query)){
			for (Integer i : track_ids) {
				pstmt.setLong(1, iD);
				pstmt.setBoolean(2, isUser);
				pstmt.setString(3, name);
				pstmt.setInt(4, i);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<String> readPlaylist(String name, long id, boolean isUser) {
		List<String> urls = new ArrayList<>();
		
		String query = "SELECT url FROM track t JOIN playlist p ON t.iD = p.track WHERE p.id = ? AND isUser = ? AND playlist_name = ? ORDER BY t.iD";
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(query)){
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
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<Integer> insertIntoTracks(List<String> playlist_complete) {
		List<Integer> result = new ArrayList<>();
		
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("INSERT INTO TRACK (url) VALUES (?)", Statement.RETURN_GENERATED_KEYS)){
			
			InsertTrackDTO insert = removeDuplicates(playlist_complete);
			
			for (String i : insert.getUnavailable()) {
				pstmt.setString(1, i);
				pstmt.executeUpdate();
				try(ResultSet rs = pstmt.getGeneratedKeys()){
					while(rs.next()) result.add(rs.getInt(1));
				}
			}
			
			result.addAll(insert.getAvailable());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	private static InsertTrackDTO removeDuplicates(List<String> test) throws Exception{
		
		List<String> available_urls = new ArrayList<>();
		List<Integer> available_id = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("SELECT iD, url FROM track WHERE url = ?")){
			for (String i : test) {
				pstmt.setString(1, i);
				try(ResultSet rs = pstmt.executeQuery()) {
					while(rs.next()) {
						available_id.add(rs.getInt(1));
						available_urls.add(rs.getString(2));
					}
				}
			}
		}
		test.removeAll(available_urls);
		return new InsertTrackDTO(available_id, test);
	}
	
	public static List<String> readRandom () throws Exception {
		List<String> urls = new ArrayList<>();
		
		try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("SELECT url FROM track ORDER BY RANDOM() LIMIT ?")){
			pstmt.setInt(1, ConfigManager.getInteger("random_count"));
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) urls.add(rs.getString(1));
				return urls;
			}
		}
	}
	
}
