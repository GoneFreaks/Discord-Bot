package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.util.UserAndGuildCommands;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteGuildPlaylistCommand extends ServerCommand {

	public DeleteGuildPlaylistCommand() {
		super(true, true, DeleteGuildPlaylistCommand.class, "Delete Guild-Playlists", "Gruwie will prompt a dropdown-menu of all Guild-Playlists of this server\nWhen an entry has been selected, Gruwie will delete this playlist");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.ADMINISTRATOR)) {
			UserAndGuildCommands.deletePlaylist(false, channel.getGuild().getIdLong(), channel, member);
		}
		else MessageManager.sendEmbedMessage(true, "**YOU DON'T HAVE THE PERMISSION TO USE THIS COMMAND**", channel, null);
	}
	
}
