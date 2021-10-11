package de.gruwie.db.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.GetDataBaseConnection;

public class PlaylistDA {
	
	public static List<String> readAllPlaylists(long iD, boolean isUser) {
		List<String> playlists = new ArrayList<>();
		
		String query = isUser? "SELECT DISTINCT playlist_name FROM user_audiotrack WHERE userId = ?" : "SELECT DISTINCT playlist_name FROM guild_audiotrack WHERE guildId = ?"; 
		try(PreparedStatement pstmt = GetDataBaseConnection.getConnection().prepareStatement(query)){
			pstmt.setLong(1, iD);
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

	public static void writePlaylist(List<AudioTrack> tracks, String name, long iD, boolean isUser) {
		
		String query = "INSERT INTO " + (isUser? "user_audiotrack" : "guild_audiotrack") + " (" + (isUser? "userId" : "guildId") + ", playlist_name, url) VALUES (?, ?, ?)";
		try(PreparedStatement pstmt = GetDataBaseConnection.getConnection().prepareStatement(query)){
			for (AudioTrack track : tracks) {
				pstmt.setLong(1, iD);
				pstmt.setString(2, name);
				pstmt.setString(3, track.getInfo().uri);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readPlaylist(String name, long id, boolean isUser) {
		List<String> urls = new ArrayList<>();
		
		String query = "SELECT url FROM " + (isUser? "user_audiotrack" : "guild_audiotrack") + " WHERE " + (isUser? "userId" : "guildId") + " = ? AND playlist_name = ?";
		try(PreparedStatement pstmt = GetDataBaseConnection.getConnection().prepareStatement(query)){
			pstmt.setLong(1, id);
			pstmt.setString(2, name);
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
	
}
