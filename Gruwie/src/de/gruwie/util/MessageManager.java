package de.gruwie.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.gruwie.db.ChannelManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	private static List<Message> storage = new LinkedList<>();
	
	public static Message sendEmbedMessage(boolean delete, String message, long guildId, String footer) {
		try {
			Message output = sendEmbedMessage(message, guildId, footer);
			if(ConfigManager.getBoolean("delete?") && delete) output.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, Filter.handler, null);
			else storage.add(output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Message sendEmbedMessage(String message, long guildId, String footer) {
		TextChannel channel = ChannelManager.getChannel(guildId);
		try {
			return channel.sendMessageEmbeds(buildEmbedMessage(message, footer).build()).complete();
		} catch (Exception e) {
			e.printStackTrace();
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
			m.editMessageEmbeds(buildEmbedMessage(message, null).build()).queue(null, Filter.handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmbedPrivateMessage(PrivateChannel channel, String message) {
		channel.sendMessageEmbeds(buildEmbedMessage(message, null).build()).queue(null, Filter.handler);
	}
	
	public static void shutdown() {
		storage.forEach((m) -> {
			m.delete().queue(null, Filter.handler);
		});
	}
}
