package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.command.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class NextCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		
		queue.next();
		
	}

}
