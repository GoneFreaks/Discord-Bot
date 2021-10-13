package de.gruwie.listener;

import de.gruwie.ConfigManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		String message_content = event.getMessage().getContentDisplay().toLowerCase();
		TextChannel channel = event.getChannel();
			
		if(message_content.startsWith(ConfigManager.getString("symbol"))) {
			String[] args = message_content.substring(1).split(" ");
				
			if(args.length > 0) {
				Message message = event.getMessage();
				try {
					if(Gruwie_Startup.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, message)) {
						MessageManager.sendEmbedMessage("**UNKNOWN COMMAND**", channel, true);
					}
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, message.getContentRaw(), message.getAuthor().getName(), channel.getGuild().getId()));
				}
				message.delete().queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		if(event.getAuthor().getId().equals(ConfigManager.getString("owner_id"))) {
			Message message = event.getMessage();
			String cmd = event.getMessage().getContentRaw().split(" ")[0].toLowerCase();
			try {
				if(Gruwie_Startup.INSTANCE.getACmdMan().performCommand(cmd, message, event.getChannel())) {
					MessageManager.sendEmbedPrivateMessage(event.getChannel(), "UNKNOWN COMMAND");
				}
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, message.getContentRaw(), message.getAuthor().getName(), "PRIVATE"));
			}
		}
		else MessageManager.sendEmbedPrivateMessage(event.getChannel(), "WHY ARE YOU SENDING MESSAGES TO A BOT?");
	}
}
