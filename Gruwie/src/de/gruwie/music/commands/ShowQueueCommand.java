package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowQueueCommand extends ServerCommand {

	public ShowQueueCommand() {
		super(true, true, ShowQueueCommand.class, null, "Show complete queue", "Display the compelte queue.\nDepending on the size of the queue, multiple messages will be send.\nThese queues are not interactive, meaning you won't see live changes while adding a track.");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		String info = queue.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs";
		MessageManager.sendEmbedMessage(false, info + "\n\n" + queue.toStringHelper(0, queue.size(), -1), channel, null);
	}

}
