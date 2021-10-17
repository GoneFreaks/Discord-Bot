package de.gruwie.db.management;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.gruwie.db.ConnectionManager;

public class DataAccess {

	public static List<TrackDTO> readTracks() throws Exception {
		List<TrackDTO> result = new ArrayList<>();
		
		try(Statement stmt = ConnectionManager.getConnection().createStatement()) {
			try(ResultSet rs = stmt.executeQuery("SELECT * FROM track WHERE genre1 IS NULL AND genre2 IS NULL AND genre3 IS NULL")){
				while(rs.next()) result.add(new TrackDTO(rs.getInt("iD"), rs.getString("url"), rs.getInt("genre1"), rs.getInt("genre2"), rs.getInt("genre3")));
			}
		}
		return result;
	}
	
}
