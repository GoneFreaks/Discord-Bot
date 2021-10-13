package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ExportPlaylistCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportUserPlaylistCommand implements ServerCommand {

	private static final String COMMAND = "exportuserplaylist";
	private static final String SHORTCUT = "eup";
	private static final String SYMBOL = null;
	private static final String DESCRIPTION = "Save the current music-queue as an account-specific playlist with the provided name (no spaces are allowed)\nEveryone can use this command";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		ExportPlaylistCommand.exportPlaylist(member, channel, message, true);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getCommand() {
		return COMMAND;
	}

	@Override
	public String getShortcut() {
		return SHORTCUT;
	}

	@Override
	public String getSymbol() {
		return SYMBOL;
	}

}
