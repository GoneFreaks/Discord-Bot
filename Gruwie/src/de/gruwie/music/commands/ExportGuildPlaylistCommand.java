package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ExportPlaylistCommand;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportGuildPlaylistCommand implements ServerCommand {

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.ADMINISTRATOR)) ExportPlaylistCommand.exportPlaylist(member, channel, message, false);
		else MessageManager.sendEmbedMessage("**YOU DON'T HAVE THE PERMISSION TO USE THIS COMMAND**", channel, true);
	}

}
