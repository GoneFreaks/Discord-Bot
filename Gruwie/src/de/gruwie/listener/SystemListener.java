package de.gruwie.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.db.ConnectionManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.Formatter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
		shardMan.addEventListener(new InteractionListener());
		Formatter.printBorderline("=");
		long startup_time = (System.currentTimeMillis() - Gruwie_Startup.start_time) / 1000;
		System.out.println("BOT online after: " + startup_time + " second" + (startup_time > 1? "s" : ""));
		Formatter.printBorderline("=");
		
		List<Guild> guilds = event.getJDA().getGuilds();
		System.out.println("Connected to:");
		for (int i = 0; i < guilds.size(); i++) {
			Guild current = guilds.get(i);
			System.out.println((i + 1) + ": " + current.getName());
			System.out.println("\tMembers: " + current.getMemberCount() + "\tOwner: <@!" + current.getOwnerId() + ">");
			if(i + 1 != guilds.size()) Formatter.printBorderline("-");
		}
		Formatter.printBorderline("=");
		
		shardMan.setActivity(Activity.listening("help"));
		shardMan.setStatus(OnlineStatus.ONLINE);
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		try {
			ChannelManager.shutdown();
			ErrorClass.shutdown();
			ConnectionManager.closeConnection();
			System.out.println("BOT has been shutdown");
			TimeUnit.SECONDS.sleep(3);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
