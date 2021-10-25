package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.db.PlaylistManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.PlaylistsDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetPlaylistsCommand extends CommandInfo implements ServerCommand {

	public GetPlaylistsCommand() {
		super(false, true, GetPlaylistsCommand.class.getSimpleName(), null, "By using this command Gruwie will prompt a dialog with some buttons below it.\nThere are three type of buttons:\n***Guild-Playlists:*** which can only be played if you're on the right server\n***User-Playlists:*** which are private and bound to your account (these playlists can be used globally)\n***Random-Playlist:*** which can be used by everyone, Gruwie will try to retrieve up to n-Tracks depending on youre config");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getBoolean("database")) {
			PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
			showPlaylists(playlists, channel);
		}
		else MessageManager.sendEmbedMessage("**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, true);
	}
	
	private static void showPlaylists(PlaylistsDTO playlists, TextChannel channel) {
		
		List<Button> buttons = new ArrayList<>();
		List<ActionRow> rows = new ArrayList<>();
		
		List<String> guild = playlists.getGuild_playlists();
		if(guild != null) {
			buttons.add(Button.primary("rand", "RANDOM"));
			for (int i = 0; i < guild.size(); i++) {
				buttons.add(Button.primary("gpgu" + guild.get(i), "GUILD: " + guild.get(i)));
				if((i+1) % 5 == 0) {
					rows.add(ActionRow.of(buttons));
					buttons = new ArrayList<>();
				}
			}
			if(buttons.size() > 0) {
				rows.add(ActionRow.of(buttons));
				buttons = new ArrayList<>();
			}
		}
		
		List<String> user = playlists.getUser_playlists();
		if(user != null) {
			for (int i = 0; i < user.size(); i++) {
				buttons.add(Button.primary("gpus" + user.get(i), "USER: " + user.get(i)));
				if((i+1) % 5 == 0) {
					rows.add(ActionRow.of(buttons));
					buttons = new ArrayList<>();
				}
			}
			if(buttons.size() > 0) rows.add(ActionRow.of(buttons));
		}
		
		TextChannel output_channel = ChannelManager.getChannel(channel.getGuild().getIdLong());
		if(rows.size() > 0) {
			MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A PLAYLIST***").build();
			MessageAction action = output_channel.sendMessageEmbeds(message_embed);
			action.setActionRows(rows).queue(null, ErrorClass.getErrorHandler());
		}
		else MessageManager.sendEmbedMessage("**NO PLAYLIST EITHER FOR YOUR ACCOUNT OR FOR THE SERVER WERE FOUND**", output_channel, true);
	}

}
