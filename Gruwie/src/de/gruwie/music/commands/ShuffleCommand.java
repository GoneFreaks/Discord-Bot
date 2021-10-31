package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.CommandInfo;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShuffleCommand extends CommandInfo {
	
	public ShuffleCommand() {
		super(false, false, ShuffleCommand.class.getSimpleName(), ":twisted_rightwards_arrows:", "Shuffle queue", "Shuffle the complete music-queue");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		queue.shuffle();
	}
}
