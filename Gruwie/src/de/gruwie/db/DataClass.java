package de.gruwie.db;

import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.TextChannel;

public class DataClass {

	private static ConcurrentHashMap<Long, Long> storage = new ConcurrentHashMap<>();
	
	public static void putChannel(long key, long value) {
		storage.put(key, value);
	}
	
	public static TextChannel getChannel(long key) {
		
		if(!storage.containsKey(key)) {
			System.out.println("IF");
			TextChannel default_channel = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(key).getDefaultChannel();
			storage.put(key, default_channel.getIdLong());
			return default_channel;
		}
		else return Gruwie_Startup.INSTANCE.getShardMan().getTextChannelById(storage.get(key));
	}
	
}
