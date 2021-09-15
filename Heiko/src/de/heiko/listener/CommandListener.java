package de.heiko.listener;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			
			if(message.startsWith("-")) {
				String[] args = message.substring(1).split(" ");
				
				if(args.length > 0) {
					Gruwie_Startup.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage());
				}
			}
		}
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		System.out.println(event.getCommandString() + "\n" + event.getName());
	}
	
}
