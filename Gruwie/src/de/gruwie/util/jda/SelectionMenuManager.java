package de.gruwie.util.jda;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.jda.selectOptions.SelectOptionAction;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class SelectionMenuManager {

	private static ConcurrentHashMap<UUID, SelectOptionAction> storage = new ConcurrentHashMap<>();
	private static Set<UUID> taken = new HashSet<>();
	
	public static UUID getUUID () {
		UUID result;
		while(true) {
			result = UUID.randomUUID();
			if(!storage.contains(result)) break;
		}
		taken.add(result);
		return result;
	}
	
	public static void putAction (UUID uuid, SelectOptionAction action) {
		storage.put(uuid, action);
		taken.remove(uuid);
	}
	
	public static void executeAction (String uuid) {
		SelectOptionAction action = storage.remove(UUID.fromString(uuid));
		if(action != null) action.perform();
	}
	
	public static void createDropdownMenu (List<SelectOptionAction> actions, MessageChannel channel, String message) {
		Builder builder = SelectionMenu.create(getUUID().toString());
		actions.forEach((k) -> {
			builder.addOptions(k);
			putAction(k.getUUID(), k);
		});
		
		MessageChannel output_channel;
		if(channel instanceof TextChannel) output_channel = ChannelManager.getChannel(((TextChannel) channel));
		else output_channel = channel;
		MessageEmbed message_embed = MessageManager.buildEmbedMessage(message, null).build();
		MessageAction action = output_channel.sendMessageEmbeds(message_embed);
		action.setActionRow(builder.build()).queue(null, Filter.handler);
	}
	
}