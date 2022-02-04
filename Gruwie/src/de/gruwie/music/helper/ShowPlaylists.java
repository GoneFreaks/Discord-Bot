package de.gruwie.music.helper;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.db.PlaylistManager;
import de.gruwie.util.EntityType;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.PlaylistsDTO;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import de.gruwie.util.selectOptions.GetOrUpdatePlaylist;
import de.gruwie.util.selectOptions.GetRandomPlaylist;
import de.gruwie.util.selectOptions.SelectOptionAction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowPlaylists {

	public static void showPlaylists(TextChannel channel, Member member, boolean isGet, EntityType type) throws TooManyPlaylistsException {
		
		PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
		
		if(type.equals(EntityType.ALL) && playlists.size() > 0) throw new TooManyPlaylistsException("Current size: " + playlists.size());
		if(type.equals(EntityType.GUILD)) {
			playlists.resetUser_playlists();
			if(playlists.size() > 24) throw new TooManyPlaylistsException("Current size: " + playlists.size());
		}
		if(type.equals(EntityType.USER)) {
			playlists.resetGuild_playlists();
			if(playlists.size() > 24) throw new TooManyPlaylistsException("Current size: " + playlists.size());
		}
			
		List<SelectOptionAction> actions = new ArrayList<>();
		if(isGet) actions.add(new GetRandomPlaylist(member, channel));
		
		if(isGet || member.hasPermission(Permission.ADMINISTRATOR)) {
			List<String> guild = playlists.getGuild_playlists();
			if(guild != null) for (String i : guild) actions.add(new GetOrUpdatePlaylist(i, member, channel, false, isGet));
		}
			
		List<String> user = playlists.getUser_playlists();
		if(user != null) for (String i : user) actions.add(new GetOrUpdatePlaylist(i, member, channel, true, isGet));
		
		SelectionMenuManager.createDropdownMenu(actions, channel, "***CHOOSE A PLAYLIST***\n\n*USER-Playlist*: Only visible to you, can be used globally\n*GUILD-Playlist*: Visible only on the server they were created on");
		
	}
	
}
