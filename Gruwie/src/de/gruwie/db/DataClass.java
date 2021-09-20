package de.gruwie.db;

import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.TextChannel;

public class DataClass {

	private static ConcurrentHashMap<Long, Long> storage;
	
	public DataClass() {
		storage = new ConcurrentHashMap<>();
	}
	
	public static void putChannel(long key, long value) {
		storage.put(key, value);
	}
	
	public static TextChannel getChannel(long key) {
		String temp = "884511240565698563";
		return Gruwie_Startup.INSTANCE.getShardMan().getTextChannelById(temp);
		
		//return storage.get(key);
	}
	
}
