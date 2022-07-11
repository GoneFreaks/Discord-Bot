package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShuffleCommand extends ServerCommand {
	
	public ShuffleCommand() {
		super(false, false, ShuffleCommand.class, null, "🔀", 2, null, null, Outputs.SHORT_DESCRIPTION_SHUFFLE, Outputs.DESCRIPTION_SHUFFLE);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		queue.shuffle();
	}
}
