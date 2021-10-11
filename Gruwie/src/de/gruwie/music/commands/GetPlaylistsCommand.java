package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.ConfigManager;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.db.PlaylistManager;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.PlaylistsDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetPlaylistsCommand implements ServerCommand {

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getBoolean("database")) {
			PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
			
			showPlaylists(playlists, channel);
		}
		else {
			MessageManager.sendEmbedMessage("**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, true);
		}
	}
	
	private static void showPlaylists(PlaylistsDTO playlists, TextChannel channel) {
		
		List<Button> buttons = new ArrayList<>();
		List<ActionRow> rows = new ArrayList<>();
		
		if(playlists.getGuild_playlists() != null) {
			for (String i : playlists.getGuild_playlists()) {
				buttons.add(Button.primary("gpgu" + i, i));
			}
			if(buttons.size() > 0 ) rows.add(ActionRow.of(buttons));
			
			buttons = new ArrayList<>();
		}
		
		List<String> user = playlists.getUser_playlists();
		if(user != null) {
			for (int i = 0; i < user.size(); i++) {
				buttons.add(Button.primary("gpus" + user.get(i), user.get(i)));
				if((i+1) % 5 == 0) {
					rows.add(ActionRow.of(buttons));
					buttons = new ArrayList<>();
				}
			}
			if(buttons.size() > 0) rows.add(ActionRow.of(buttons));
		}
		
		TextChannel output_channel = ChannelManager.getChannel(channel);
		if(rows.size() > 0) {
			MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A PLAYLIST***").build();
			MessageAction action = output_channel.sendMessageEmbeds(message_embed);
			action.setActionRows(rows).queue();
		}
		else {
			MessageManager.sendEmbedMessage("**NO PLAYLIST EITHER FOR YOUR ACCOUNT AND FOR THE SERVER WERE FOUND**", output_channel, true);
		}
	}

}
