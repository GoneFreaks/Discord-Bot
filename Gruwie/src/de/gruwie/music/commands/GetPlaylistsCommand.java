package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.db.PlaylistManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Filter;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.PlaylistsDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetPlaylistsCommand extends ServerCommand {

	public GetPlaylistsCommand() {
		super(false, true, GetPlaylistsCommand.class, "Load saved playlists", "If you're using this command and a number Gruwie will load a random playlist of the given size\nBy using only this command Gruwie will prompt a dialog with some buttons below it.\nThere are three type of buttons:\n***Guild-Playlists:*** which can only be played if you're on the right server\n***User-Playlists:*** which are private and bound to your account (these playlists can be used globally)\n***Random-Playlist:*** which can be used by everyone, Gruwie will try to retrieve up to n-Tracks depending on youre config");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		
		if(ConfigManager.getBoolean("database") && (args.length == 1 || args.length == 2)) {
			if(args.length == 1) {
				PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
				showPlaylists(playlists, channel);
			}
			if(args.length == 2) {
				try {
					int count = Integer.parseInt(args[1]);
					if(count <= ConfigManager.getInteger("max_queue_size")) PlaylistManager.randPlaylist(member, channel, count);
					else MessageManager.sendEmbedMessage(true, "**THE GIVEN NUMBER (" + count + ") IS TOO BIG, PROVIDE A SMALLER NUMBER (<" + ConfigManager.getInteger("max_queue_size") + ")**", channel, null);
				} catch (Exception e) {
					MessageManager.sendEmbedMessage(true, "**YOU HAVE TO PROVIDE A VALID NUMBER**", channel, null);
				}
			}
			
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
	private static void showPlaylists(PlaylistsDTO playlists, TextChannel channel) {
		
		Builder builder = SelectionMenu.create("gpsm");
		builder.addOption("Random", "rand", "Load some random tracks");
		
		List<String> guild = playlists.getGuild_playlists();
		if(guild != null) {
			for (String i : guild) {
				builder.addOption("GUILD: " + i, "gpgu" + i);
			}
		}
		
		List<String> user = playlists.getUser_playlists();
		if(user != null) {
			for (String i : user) {
				builder.addOption("USER: " + i, "gpus" + i);
			}
		}
		
		TextChannel output_channel = ChannelManager.getChannel(channel);
		MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A PLAYLIST***\n\n*USER-Playlist*: Only visible to you, can be used globally\n*GUILD-Playlist*: Visible only on the server they were created on", null).build();
		MessageAction action = output_channel.sendMessageEmbeds(message_embed);
		action.setActionRow(builder.build()).queue(null, Filter.handler);
	}

}
