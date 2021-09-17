package de.gruwie.util;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	private static final long DELETE_TIME = 7000;

	public static void sendMessage (String message, TextChannel channel, boolean delete) {
		
		if(delete) sendMessage(message, channel, DELETE_TIME);
		else channel.sendMessage(message).queue();
	}
	
	public static void sendMessage (String message, TextChannel channel, long delete) {
		
		channel.sendMessage(message).complete().delete().queueAfter(delete, TimeUnit.MILLISECONDS);	
		
	}
	
	public static void sendEmbedMessage (String message, TextChannel channel, boolean delete) {
		
		if(delete) {
			sendEmbedMessage(message, channel, DELETE_TIME);
		}
		else channel.sendMessageEmbeds(buildEmbedMessage(message)).queue();
	}
	
	public static void sendEmbedMessage (String message, TextChannel channel, long delete) {
		channel.sendMessageEmbeds(buildEmbedMessage(message)).complete().delete().queueAfter(delete, TimeUnit.MILLISECONDS);
	}
	
	private static MessageEmbed buildEmbedMessage (String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message);
		builder.setColor(0x58ACFA);
		return builder.build();
	}
}
