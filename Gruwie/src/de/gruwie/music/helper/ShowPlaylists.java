package de.gruwie.music.helper;

import java.util.List;
import java.util.UUID;

import de.gruwie.db.ChannelManager;
import de.gruwie.db.PlaylistManager;
import de.gruwie.util.Filter;
import de.gruwie.util.MessageManager;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.PlaylistsDTO;
import de.gruwie.util.selectOptions.GetOrUpdatePlaylist;
import de.gruwie.util.selectOptions.GetRandomPlaylist;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class ShowPlaylists {

	public static void showPlaylists(TextChannel channel, Member member, boolean isGet) {
		
		PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
		
		Builder builder = SelectionMenu.create(SelectionMenuManager.getUUID().toString());
		
		if(isGet) {
			UUID value = SelectionMenuManager.getUUID();
			GetRandomPlaylist select = new GetRandomPlaylist(value, member, channel);
			SelectionMenuManager.putAction(value, select);
			builder.addOptions(select);
		}
		
		if(isGet || member.hasPermission(Permission.ADMINISTRATOR)) {
			List<String> guild = playlists.getGuild_playlists();
			if(guild != null) {
				for (String i : guild) {
					UUID value_guild = SelectionMenuManager.getUUID();
					GetOrUpdatePlaylist select_guild = new GetOrUpdatePlaylist(i, value_guild, member, channel, false, isGet);
					SelectionMenuManager.putAction(value_guild, select_guild);
					builder.addOptions(select_guild);
				}
			}
		}
		
		List<String> user = playlists.getUser_playlists();
		if(user != null) {
			for (String i : user) {
				UUID value_user = SelectionMenuManager.getUUID();
				GetOrUpdatePlaylist select_user = new GetOrUpdatePlaylist(i, value_user, member, channel, true, isGet);
				SelectionMenuManager.putAction(value_user, select_user);
				builder.addOptions(select_user);
			}
		}
		
		TextChannel output_channel = ChannelManager.getChannel(channel);
		MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A PLAYLIST***\n\n*USER-Playlist*: Only visible to you, can be used globally\n*GUILD-Playlist*: Visible only on the server they were created on", null).build();
		MessageAction action = output_channel.sendMessageEmbeds(message_embed);
		action.setActionRow(builder.build()).queue(null, Filter.handler);
	}
	
}
