package de.gruwie.db;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelManager {

	public static boolean withDataBase = true;
	private static ConcurrentHashMap<Long, Long> storage = new ConcurrentHashMap<>();
	private static Set<Long> modified = new HashSet<>();
	
	public static void shutdown() {
		
		if(!withDataBase) return;
		
		if(modified.size() > 0) {
			System.out.println("ChannelManager: Daten werden geschrieben");
		}
	}
	
	public static void startup() {
		
		if(!withDataBase) return;
		
		System.out.println("ChannelManager: Daten werden gelesen");
	}
	
	public static void putChannel(long guild_id, long channel_id) {
		storage.put(guild_id, channel_id);
		modified.add(guild_id);
	}
	
	public static TextChannel getChannel(TextChannel channel) {
		long guild_id = channel.getGuild().getIdLong();
		return getChannel(guild_id);
	}
	
	public static TextChannel getChannel(long guild_id) {
		if(!storage.containsKey(guild_id)) {
			TextChannel default_channel = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id).getDefaultChannel();
			putChannel(guild_id, default_channel.getIdLong());
			return default_channel;
		}
		else return Gruwie_Startup.INSTANCE.getShardMan().getTextChannelById(storage.get(guild_id));
	}
	
}
