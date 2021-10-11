package de.gruwie.listener;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.db.PlaylistManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.Formatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class SystemListener extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		
		ShardManager shardMan = Gruwie_Startup.INSTANCE.getShardMan();
		shardMan.addEventListener(new CommandListener());
		shardMan.addEventListener(new EmoteListener());
		Formatter.printBorderline("=");
		
		long startup_time = (System.currentTimeMillis() - Gruwie_Startup.start_time) / 1000;
		System.out.println("BOT online after: " + startup_time + " second" + (startup_time > 1? "s" : ""));
		Formatter.printBorderline("=");
		
		List<Guild> guilds = event.getJDA().getGuilds();
		System.out.println("Connected to:");
		for (int i = 0; i < guilds.size(); i++) {
			Guild current = guilds.get(i);
			System.out.println((i + 1) + ": " + current.getName());
			System.out.println("\tMembers: " + current.getMemberCount() + " Owner: <@!" + current.getOwnerId() + ">");
			if(i + 1 != guilds.size()) Formatter.printBorderline("-");
		}
		Formatter.printBorderline("=");
		
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		
		ChannelManager.shutdown();
		ErrorClass.shutdown();
		System.out.println("BOT has been shutdown");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		
		event.deferEdit().queue();
		String type = event.getComponentId().substring(0, 4);
		String data = event.getComponentId().substring(4);
		
		switch (type) {
			case "reth": {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(event.getGuild().getIdLong());
				Queue queue = controller.getQueue();
				queue.removeTrack(data);
				break;
			}
			case "gpus": {
				try {
					PlaylistManager.playPlaylist(event.getTextChannel(), event.getMember(), data, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			case "gpgu": {
				try {
					PlaylistManager.playPlaylist(event.getTextChannel(), event.getMember(), data, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		event.getMessage().delete().queue();
	}
	
}
