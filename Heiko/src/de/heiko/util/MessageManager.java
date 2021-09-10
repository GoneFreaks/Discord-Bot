package de.heiko.util;

import java.util.concurrent.TimeUnit;

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
}
