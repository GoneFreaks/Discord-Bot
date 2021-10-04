package de.gruwie.util;

import java.util.concurrent.TimeUnit;

import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	private static final long DELETE_TIME = 7000;
	
	public static Message sendEmbedMessage (String message, TextChannel channel, boolean delete) {
		try {
			if(delete) {
				return sendEmbedMessage(message, channel, DELETE_TIME);
			}
			else return channel.sendMessageEmbeds(buildEmbedMessage(message)).complete();
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM"));
			return null;
		}
	}
	
	public static Message sendEmbedMessage (String message, TextChannel channel, long delete) {
		try {
			Message output = channel.sendMessageEmbeds(buildEmbedMessage(message)).complete();
			output.delete().queueAfter(delete, TimeUnit.MILLISECONDS);
			return output;
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM"));
			return null;
		}
	}
	
	private static MessageEmbed buildEmbedMessage (String message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message);
		builder.setColor(0x58ACFA);
		return builder.build();
	}
	
	public static void editMessage (Message m, String message) {
		
		try {
			m.editMessageEmbeds(buildEmbedMessage(message)).queue();
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "MESSAGE-MANAGER", "SYSTEM"));
		}
	}
	
	public static void sendEmbedPrivateMessage(PrivateChannel channel, String message) {
		channel.sendMessageEmbeds(buildEmbedMessage(message)).queue();
	}
}
