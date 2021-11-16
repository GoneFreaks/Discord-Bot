package de.gruwie.util;

import java.util.concurrent.TimeUnit;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	public static Message sendEmbedMessage(String message, long guildId) {
		
		TextChannel channel = ChannelManager.getChannel(guildId);
		try {
			
			if(ConfigManager.getBoolean("delete?")) {
				Message output = channel.sendMessageEmbeds(buildEmbedMessage(message).build()).complete();
				output.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, ErrorClass.getErrorHandler(), null);
				return output;
			}
			else return channel.sendMessageEmbeds(buildEmbedMessage(message).build()).complete();
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM", channel.getGuild().getId()));
			return null;
		}
	}
	
	public static Message sendEmbedMessage(long guildId, String message) {
		TextChannel channel = ChannelManager.getChannel(guildId);
		return channel.sendMessageEmbeds(buildEmbedMessage(message).build()).complete();
	}
	
	public static Message sendEmbedMessage(String message, TextChannel channel) {return sendEmbedMessage(message, channel.getGuild().getIdLong());}
	
	public static EmbedBuilder buildEmbedMessage (String message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message);
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
			m.editMessageEmbeds(buildEmbedMessage(message).build()).queue(null, ErrorClass.getErrorHandler());
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM", m.getGuild().getId()));
		}
	}
	
	public static void sendEmbedPrivateMessage(PrivateChannel channel, String message) {
		channel.sendMessageEmbeds(buildEmbedMessage(message).build()).queue(null, ErrorClass.getErrorHandler());
	}
}
