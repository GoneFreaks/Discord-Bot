package de.gruwie.util;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.db.PlaylistManager;
import de.gruwie.util.dto.CheckTrackDTO;
import de.gruwie.util.dto.PlaylistsDTO;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.jda.selectOptions.SOA.GetPlaylistSOA;
import de.gruwie.util.jda.selectOptions.SOA.GetRandomPlaylistSOA;
import de.gruwie.util.jda.selectOptions.SOA.SetOrRemoveTrackSOA;
import de.gruwie.util.jda.selectOptions.SOA.UpdatePlaylistSOA;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Dropdown {

	public static void getPlaylists(TextChannel channel, Member member, boolean isGet) {
		
		PlaylistsDTO playlists = PlaylistManager.getPlaylists(channel.getGuild().getIdLong(), member.getIdLong());
			
		List<SelectOptionAction> actions = new ArrayList<>();
		if(isGet) actions.add(new GetRandomPlaylistSOA(member, channel));
		
		if(isGet || member.hasPermission(Permission.ADMINISTRATOR)) {
			List<String> guild = playlists.getGuild_playlists();
			if(guild != null) for (String i : guild) {
				SelectOptionAction action = isGet? new GetPlaylistSOA(i, member, channel, false) : new UpdatePlaylistSOA(i, member, false);
				actions.add(action);
			}
		}
			
		List<String> user = playlists.getUser_playlists();
		if(user != null) for (String i : user) {
			SelectOptionAction action = isGet? new GetPlaylistSOA(i, member, channel, true) : new UpdatePlaylistSOA(i, member, true);
			actions.add(action);
		}
		
		SelectionMenuManager.createDropdownMenu(actions, isGet? channel : member.getUser().openPrivateChannel().complete(), "***CHOOSE A PLAYLIST***\n\n*USER-Playlist*: Only visible to you, can be used globally\n*GUILD-Playlist*: Visible only on the server they were created on");
		
	}
	
	public static void multipleEntriesFound (String message, List<CheckTrackDTO> track_list, TextChannel channel, Member member, boolean isSetter) {
		
		if(track_list.size() > 5) {
			MessageManager.sendEmbedMessage(true, "**THE PROVIDED INPUT IS TOO AMBIGUOUS**", channel, null);
		}
		
		StringBuilder c = new StringBuilder("**Multiple entries have been found:**\n\n*");
		
		for(int i = 0; i < track_list.size(); i++) {
			c.append((i+1) + ": " + track_list.get(i).getTitleOriginal() + (i+1 == track_list.size()? "*" : "\n"));
		}
		
		c.append("\n\n**Which track should be deleted?**");
		
		List<SelectOptionAction> actions = new ArrayList<>();
		track_list.forEach((k) -> {
			actions.add(new SetOrRemoveTrackSOA(k.getTitleOriginal(), member, channel, isSetter));
		});
		SelectionMenuManager.createDropdownMenu(actions, channel, c.toString());
	}
	
}
