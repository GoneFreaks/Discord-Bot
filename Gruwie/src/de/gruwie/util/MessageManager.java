package de.gruwie.util;

import java.util.concurrent.TimeUnit;

import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	public static Message sendEmbedMessage (String message, TextChannel channel, boolean delete) {
		try {
			if(delete && ConfigManager.getBoolean("delete?")) {
				return sendEmbedMessage(message, channel, ConfigManager.getInteger("delete_time"));
			}
			else return channel.sendMessageEmbeds(buildEmbedMessage(message).build()).complete();
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM", channel.getGuild().getId()));
			return null;
		}
	}
	
	private static Message sendEmbedMessage (String message, TextChannel channel, long delete) throws Exception {
		
		Message output = channel.sendMessageEmbeds(buildEmbedMessage(message).build()).complete();
		output.delete().queueAfter(delete, TimeUnit.MILLISECONDS, null, ErrorClass.getErrorHandler(), null);
		return output;
	}
	
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
