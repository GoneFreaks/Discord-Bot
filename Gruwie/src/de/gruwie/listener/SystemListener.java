package de.gruwie.listener;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.Formatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class SystemListener extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		
		ShardManager shardMan = Gruwie_Startup.INSTANCE.getShardMan();
		shardMan.addEventListener(new CommandListener());
		shardMan.addEventListener(new EmoteListener());
		
		Formatter.printBorderline();
		List<Guild> guilds = event.getJDA().getGuilds();
		System.out.println("BOT is online");
		System.out.println("Connected to " + guilds.size() + " Guild" + ((guilds.size() > 1)? "s" : ""));
		
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		
		List<Object> listener = event.getJDA().getRegisteredListeners();
		for (Object i : listener) {
			event.getJDA().removeEventListener(i);
		}
		
		ChannelManager.shutdown();
		ErrorClass.shutdown();
		System.out.println("BOT has been shutdown");
	}
	
}
