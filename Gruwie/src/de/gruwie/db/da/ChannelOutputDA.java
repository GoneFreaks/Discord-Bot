package de.gruwie.db.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.db.ConnectionManager;

public class ChannelOutputDA {

	public static ConcurrentHashMap<Long, Long> readOutputChannels(){
		ConcurrentHashMap<Long, Long> result = new ConcurrentHashMap<>();
		
		try (Connection cn = ConnectionManager.getConnection(true)){
			try (Statement stmt = cn.createStatement()){
				try(ResultSet rs = stmt.executeQuery("SELECT * FROM OUTPUT_CHANNEL")) {
					while(rs.next()) {
						result.put(rs.getLong("guildId"), rs.getLong("channelId"));
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void writeOutputChannels(Set<Long> modified, ConcurrentHashMap<Long, Long> channels) {
		
		try (Connection cn = ConnectionManager.getConnection(false)){
			try (PreparedStatement pstmt = cn.prepareStatement("UPDATE output_channel SET channelId = ? WHERE guildId = ?")){
				
				for (Long i : modified) {
					pstmt.setLong(1, channels.get(i));
					pstmt.setLong(2, i);
					pstmt.executeUpdate();
				}
				cn.commit();
			} catch (Exception e) {
				cn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
