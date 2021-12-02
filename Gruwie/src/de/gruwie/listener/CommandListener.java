package de.gruwie.listener;

import de.gruwie.CommandManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.GuessMeantCommand;
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
		String symbol = ConfigManager.getString("symbol");
		
		if(message_content.startsWith(symbol) || message_content.equals("help")) {
			String[] args = message_content.replaceFirst(symbol, "").split(" ");
			
			if(args.length > 0) {
				Message message = event.getMessage();
				try {
					CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
					if(cmdMan.perform(args[0], event.getMember(), channel, message)) {
						if(ConfigManager.getBoolean("guess_command")) {
							String meant_cmd = GuessMeantCommand.probableCommand(cmdMan.getCommandArray(), args[0], symbol);
							if(meant_cmd == null) MessageManager.sendEmbedMessage(true, "**I DON'T KNOW THIS COMMAND (╯°□°）╯︵ ┻━┻**", channel, null);
							else MessageManager.sendEmbedMessage(true, "**MAYBE YOU WANTED TO USE: " + meant_cmd + "**", channel, null);
						}
					}
				} catch (Exception e) {
						ErrorClass.reportError(new ErrorDTO(e, message.getContentRaw(), message.getAuthor().getName(), channel.getGuild().getId()));
				}
				message.delete().queue(null, ErrorClass.getErrorHandler());
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
