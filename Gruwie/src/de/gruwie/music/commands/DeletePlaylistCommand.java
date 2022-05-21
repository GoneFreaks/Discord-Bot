package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.jda.selectOptions.SOA.DeletePlaylistSOA;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeletePlaylistCommand extends ServerCommand {

	public DeletePlaylistCommand() {
		super(true, true, DeletePlaylistCommand.class, Outputs.SHORT_DESCRIPTION_DELETEPLAYLIST, Outputs.DESCRIPTION_DELETEPLAYLIST);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(ConfigManager.getDatabase()) {
			List<String> server_playlists = PlaylistDA.readAllPlaylists(member.getGuild().getIdLong(), false);
			List<String> user_playlists = PlaylistDA.readAllPlaylists(member.getUser().getIdLong(), true);
			if(server_playlists.size() > 0 || user_playlists.size() > 0) {
				List<SelectOptionAction> actions = new ArrayList<>();
				PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
				
				server_playlists.forEach((k) -> {
					actions.add(new DeletePlaylistSOA(k, false, member.getGuild().getIdLong(), privateChannel));
				});
				user_playlists.forEach((k) -> {
					actions.add(new DeletePlaylistSOA(k, true, member.getUser().getIdLong(), privateChannel));
				});
				
				SelectionMenuManager.createDropdownMenu(actions, privateChannel, "**CHOOSE THE PLAYLIST WHICH SHOULD BE DELETED**");
			}
			else MessageManager.sendEmbedMessage(true, Outputs.NOTHING_FOUND, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.DATABASE, channel);
	}
	
}
