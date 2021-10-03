package de.gruwie.listener;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SystemListener extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		
		if(Gruwie_Startup.configuration > 1) return;
		
		List<Guild> guilds = event.getJDA().getGuilds();
		for (Guild i : guilds) {
			MessageManager.sendEmbedMessage("***GRUWIE IST ONLINE***", ChannelManager.getChannel(i.getIdLong()), true);
		}
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		ChannelManager.shutdown();
		ErrorClass.shutdown();
		System.out.println("BOT has been shutdown");
	}
	
	@Override
	public void onException(ExceptionEvent event) {
		System.out.println("onException\n" + event.getCause().getMessage());
	}
	
}
