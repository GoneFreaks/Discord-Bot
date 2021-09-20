package de.gruwie.listener;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		String message_content = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			
			if(message_content.startsWith("-")) {
				String[] args = message_content.substring(1).split(" ");
				
				if(args.length > 0) {
					Message message = event.getMessage();
					Gruwie_Startup.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, message);
					message.delete().queue();
				}
			}
		}
	}
	
}
