package de.gruwie.util.jda;

import java.util.concurrent.TimeUnit;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageManager {
	
	public static Message sendEmbedMessage(boolean delete, Outputs message, long guildId) {
		try {
			Message output = sendMessage(message.getValue(), guildId, null);
			if(ConfigManager.getBoolean("delete?") && delete) output.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, Filter.handler, null);
			else MessageHolder.add(output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Message sendMessage(String message, long guildId, String footer) {
		TextChannel channel = ChannelManager.getChannel(guildId);
		Message output = channel.sendMessageEmbeds(buildEmbedMessage(message, footer).build()).complete();
		return output;
	}
	
	public static Message sendEmbedMessage(boolean delete, Outputs message, TextChannel channel) {return sendEmbedMessage(delete, message, channel.getGuild().getIdLong());}
	
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
		m.editMessageEmbeds(buildEmbedMessage(message, null).build()).queue(null, Filter.handler);
	}
	
	public static void sendEmbedPrivateMessage(PrivateChannel channel, String message, boolean delete) {
		channel.sendMessageEmbeds(buildEmbedMessage(message, null).build()).queue((m) -> {
			if(!delete) MessageHolder.add(m);
			else m.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, Filter.handler, null);
		}, Filter.handler);
	}
	
	public static Message sendEmbedMessageVariable(boolean delete, String message, long guildId) {
		return sendEmbedMessageVariable(delete, message, guildId, null);
	}
	
	public static Message sendEmbedMessageVariable(boolean delete, String message, long guildId, Outputs footer) {
		Message output = sendMessage(message, guildId, null);
		if(ConfigManager.getBoolean("delete?") && delete) output.delete().queueAfter(ConfigManager.getInteger("delete_time"), TimeUnit.MILLISECONDS, null, Filter.handler, null);
		else MessageHolder.add(output);
		return output;
	}
}
