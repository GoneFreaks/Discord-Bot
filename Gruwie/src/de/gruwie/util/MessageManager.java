package de.gruwie.util;

import java.util.concurrent.TimeUnit;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	public static Message sendEmbedMessage(boolean delete, String message, long guildId, String footer) {
		TextChannel channel = ChannelManager.getChannel(guildId);
		try {
			if(ConfigManager.getBoolean("delete?") && delete) {
				Message output = channel.sendMessageEmbeds(buildEmbedMessage(message, footer).build()).complete();
				output.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, ErrorClass.getErrorHandler(), null);
				return output;
			}
			else return channel.sendMessageEmbeds(buildEmbedMessage(message, footer).build()).complete();
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM", channel.getGuild().getId()));
			return null;
		}
	}
	
	public static Message sendEmbedMessage(boolean delete, String message, TextChannel channel, String footer) {return sendEmbedMessage(delete, message, channel.getGuild().getIdLong(), footer);}
	
	public static EmbedBuilder buildEmbedMessage (String message, String footer) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message);
		if(footer != null) builder.setFooter(footer);
		String color = ConfigManager.getString("embed-color");
		try {
			builder.setColor(Integer.decode(color));
		} catch (Exception e) {
			builder.setColor(0x58ACFA);
		}
		return builder;
	}
	
	public static void editMessage (Message m, String message) {
		
		try {
			m.editMessageEmbeds(buildEmbedMessage(message, null).build()).queue(null, ErrorClass.getErrorHandler());
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM", m.getGuild().getId()));
		}
	}
	
	public static void sendEmbedPrivateMessage(PrivateChannel channel, String message) {
		channel.sendMessageEmbeds(buildEmbedMessage(message, null).build()).queue(null, ErrorClass.getErrorHandler());
	}
}
