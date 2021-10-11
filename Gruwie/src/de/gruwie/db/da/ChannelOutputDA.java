package de.gruwie.db.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.db.GetDataBaseConnection;

public class ChannelOutputDA {

	public static ConcurrentHashMap<Long, Long> readOutputChannels(){
		ConcurrentHashMap<Long, Long> result = new ConcurrentHashMap<>();
		
		try (Statement stmt = GetDataBaseConnection.getConnection().createStatement()){
			try(ResultSet rs = stmt.executeQuery("SELECT * FROM OUTPUT_CHANNEL")) {
				while(rs.next()) {
					result.put(rs.getLong("guildId"), rs.getLong("channelId"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void writeOutputChannels(Set<Long> modified, ConcurrentHashMap<Long, Long> channels) {
		
		deleteModifiedData(modified);
		
		try (PreparedStatement pstmt = GetDataBaseConnection.getConnection().prepareStatement("INSERT INTO output_channel (channelId, guildId) VALUES(?,?)")){
		
			for (Long i : modified) {
				pstmt.setLong(1, channels.get(i));
				pstmt.setLong(2, i);
				pstmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteModifiedData(Set<Long> modified) {
		try (PreparedStatement pstmt = GetDataBaseConnection.getConnection().prepareStatement("DELETE FROM output_channel WHERE guildId = ?")){
			
			for (Long i : modified) {
				pstmt.setLong(1, i);
				pstmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
