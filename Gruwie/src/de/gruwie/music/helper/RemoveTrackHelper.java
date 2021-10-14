package de.gruwie.music.helper;

import java.util.List;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.CheckTrackDTO;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class RemoveTrackHelper {

	public static void multipleFound (List<CheckTrackDTO> track_list, TextChannel channel) {
		
		if(track_list.size() > 5) {
			return;
		}
		
		StringBuilder c = new StringBuilder("**Multiple entries have been found:**\n\n*");
		
		for(int i = 0; i < track_list.size(); i++) {
			c.append((i+1) + ": " + track_list.get(i).getTitle() + (i+1 == track_list.size()? "*" : "\n"));
		}
		
		c.append("\n\n**Which track should be deleted?**");
		
		TextChannel output_channel = ChannelManager.getChannel(channel);
		MessageEmbed message_embed = MessageManager.buildEmbedMessage(c.toString()).build();
		MessageAction action = output_channel.sendMessageEmbeds(message_embed);
		
		Builder builder = SelectionMenu.create("reth");
		for (CheckTrackDTO i : track_list) {
			builder.addOption(i.getTitleOriginal(), i.getTitleOriginal());
		}
		action.setActionRow(builder.build()).queue();
	}
	
}
