package de.gruwie.music.helper;

import java.util.List;
import java.util.UUID;

import de.gruwie.db.ChannelManager;
import de.gruwie.util.Filter;
import de.gruwie.util.MessageManager;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.CheckTrackDTO;
import de.gruwie.util.selectOptions.SetOrRemoveTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class MultipleEntriesFound {

	public static void promptDialog (String message, List<CheckTrackDTO> track_list, TextChannel channel, Member member, boolean isSetter) {
		
		if(track_list.size() > 5) {
			return;
		}
		
		StringBuilder c = new StringBuilder("**Multiple entries have been found:**\n\n*");
		
		for(int i = 0; i < track_list.size(); i++) {
			c.append((i+1) + ": " + track_list.get(i).getTitle() + (i+1 == track_list.size()? "*" : "\n"));
		}
		
		c.append("\n\n**Which track should be deleted?**");
		
		TextChannel output_channel = ChannelManager.getChannel(channel);
		MessageEmbed message_embed = MessageManager.buildEmbedMessage(c.toString(), null).build();
		MessageAction action = output_channel.sendMessageEmbeds(message_embed);
		
		Builder builder = SelectionMenu.create(SelectionMenuManager.getUUID().toString());
		for (CheckTrackDTO i : track_list) {
			UUID value = SelectionMenuManager.getUUID();
			SetOrRemoveTrack select = new SetOrRemoveTrack(i.getTitleOriginal(), value, member, channel, isSetter);
			SelectionMenuManager.putAction(value, select);
			builder.addOptions(select);
		}
		action.setActionRow(builder.build()).queue(null, Filter.handler);
	}
	
}
