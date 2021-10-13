package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RepeatCommand implements ServerCommand {

	private static final String COMMAND = "repeat";
	private static final String SHORTCUT = null;
	private static final String SYMBOL = ":repeat:";
	private static final String DESCRIPTION = "If active playing a track will not affect the music-queue, if it is not active the next track will be pulled/removed from the music-queue";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		queue.changeRepeat();
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
