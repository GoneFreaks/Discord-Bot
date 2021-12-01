package de.gruwie.db.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.gruwie.db.ConnectionManager;

public class PlayedDA {

	private static int alreadyCreated(long userId, String url) throws Exception {
		
		try(PreparedStatement trackAvailable = ConnectionManager.getConnection().prepareStatement("SELECT iD FROM track WHERE url = ?")) {
			trackAvailable.setString(1, url);
			try(ResultSet r = trackAvailable.executeQuery()) {
				if(r.next()) {
					int trackId = r.getInt(1);
					try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("SELECT * FROM played WHERE userId = ? AND trackId = ?")) {
						pstmt.setLong(1, userId);
						pstmt.setInt(2, trackId);
						try(ResultSet rs = pstmt.executeQuery()) {
							if(rs.next()) return trackId;
							else {
								try(PreparedStatement create = ConnectionManager.getConnection().prepareStatement("INSERT INTO played (userId, trackId) VALUES (?, ?)")) {
									create.setLong(1, userId);
									create.setInt(2, trackId);
									create.executeUpdate();
									return trackId;
								} 
							}
						}
					}
				}
				else return -1;
			}
		}
	}
	
	public static void incrementCount(long userId, String url) {
		try {
			int trackId = alreadyCreated(userId, url);
			if(trackId != -1) {
				try(PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement("UPDATE played SET count = count + 1 WHERE userId = ? AND trackId = ?")) {
					pstmt.setLong(1, userId);
					pstmt.setInt(2, trackId);
					pstmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
