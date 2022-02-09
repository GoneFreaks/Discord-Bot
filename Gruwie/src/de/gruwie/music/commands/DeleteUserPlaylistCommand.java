package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.util.UserAndGuildCommands;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteUserPlaylistCommand extends ServerCommand {

	public DeleteUserPlaylistCommand() {
		super(true, true, DeleteUserPlaylistCommand.class, "Delete User-Playlists", "Gruwie will prompt a dropdown-menu of all User-Playlists of the user which has used this command\nWhen an entry has been selected, Gruwie will delete this playlist");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getDatabase()) {
			UserAndGuildCommands.deletePlaylist(true, member.getIdLong(), channel, member);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
}
