package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class LoopCommand extends ServerCommand {
	
	public LoopCommand() {
		super(false, false, LoopCommand.class, Outputs.SHORT_DESCRIPTION_LOOP, Outputs.DESCRIPTION_LOOP);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		queue.changeRepeat();
	}

}
