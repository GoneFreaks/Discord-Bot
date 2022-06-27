package de.gruwie.db;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.da.ChannelOutputDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelManager {
	
	private static ConcurrentHashMap<Long, Long> storage = new ConcurrentHashMap<>();
	private static Set<Long> modified = new HashSet<>();
	
	public static void shutdown() {
		GruwieUtilities.log();
		if(ConfigManager.getDatabase() && modified.size() > 0) {
			GruwieUtilities.log("changed output-channels " + modified);
			ChannelOutputDA.writeOutputChannels(modified, storage);
		}
	}
	
	public static void startup() {
		GruwieUtilities.log();
		if(ConfigManager.getDatabase()) {
			storage = ChannelOutputDA.readOutputChannels();
		}
	}
	
	public static void putChannel(long guild_id, long channel_id) {
		GruwieUtilities.log();
		storage.put(guild_id, channel_id);
		modified.add(guild_id);
	}
	
	public static TextChannel getChannel (TextChannel channel) {
		GruwieUtilities.log();
		return getChannel(channel.getGuild().getIdLong());
	}
	
	public static TextChannel getChannel(long guild_id) {
		GruwieUtilities.log();
		if(!storage.containsKey(guild_id)) {
			TextChannel default_channel = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id).getDefaultChannel();
			GruwieUtilities.log("no entry for guild [" + guild_id + "] default channel " + default_channel.getName());
			putChannel(guild_id, default_channel.getIdLong());
			return default_channel;
		}
		else return Gruwie_Startup.INSTANCE.getShardMan().getTextChannelById(storage.get(guild_id));
	}
	
}
