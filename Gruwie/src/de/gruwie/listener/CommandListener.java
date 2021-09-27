package de.gruwie.listener;

import javax.annotation.Nonnull;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.ErrorDTO;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		String message_content = event.getMessage().getContentDisplay();
		TextChannel channel = event.getChannel();
			
		if(message_content.startsWith("-")) {
			String[] args = message_content.substring(1).split(" ");
				
			if(args.length > 0) {
				Message message = event.getMessage();
				try {
					Gruwie_Startup.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, message);
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, message.getContentRaw(), message.getAuthor().getName()));
				}
				message.delete().queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		System.out.println("onPrivateMessageReceived");
	}
	
}
