package de.gruwie.listener;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageHolder;
import de.gruwie.util.Threadpool;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.sharding.ShardManager;

public class SystemListener extends ListenerAdapter {

	public static JDA jda;
	
	@Override
	public void onReady(ReadyEvent event) {
		ShardManager shardMan = Gruwie_Startup.INSTANCE.getShardMan();
		shardMan.addEventListener(new CommandListener());
		shardMan.addEventListener(new InteractionListener());
		
		jda = event.getJDA();
		
		List<Guild> guilds = event.getJDA().getGuilds();
		StringBuilder b = new StringBuilder();
		b.append("Connected to:\n");
		for (int i = 0; i < guilds.size(); i++) {
			if(i != 0) b.append("\n");
			Guild current = guilds.get(i);
			b.append("\t\t" + current.getName());
			b.append("\tMembers: " + current.getMemberCount() + "\tOwner: " + jda.retrieveUserById(current.getOwnerId()).complete().getName());
		}
		
		GruwieUtilities.logMeta(b.toString());
		
		shardMan.setStatus(OnlineStatus.ONLINE);
		shardMan.setActivity(Activity.listening("help"));
		MessageHolder.start();
		long startup_time = Math.round((System.currentTimeMillis() - Gruwie_Startup.start_time) / 1000.0);
		GruwieUtilities.logMeta("BOT online after: " + startup_time + " second" + (startup_time > 1? "s" : ""));
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		try {
			ChannelManager.shutdown();
			Threadpool.shutdown();
			System.out.println("BOT is offline");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onMessageDelete(MessageDeleteEvent event) {
		Threadpool.execute(() -> {
			MessageHolder.checkMessage(event.getMessageId());
		});
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		long guild_id = event.getGuild().getIdLong();
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		long channel_id = controller.getVoiceChannel().getIdLong();
		long left_channel_id = event.getChannelLeft().getIdLong();
		if(channel_id == left_channel_id && event.getChannelLeft().getMembers().size() < 2) {
			AudioManager manager = event.getGuild().getAudioManager();
			controller.getPlayer().stopTrack();
			controller.getQueue().clearQueue();
			manager.closeAudioConnection();
		}
	}
}