package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowQueueCommand extends ServerCommand {

	public ShowQueueCommand() {
		super(false, true, ShowQueueCommand.class, null, -1, "Show complete queue", "Show complete queue, all messages of this type will be deleted during shutdown");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			Queue queue = controller.getQueue();
			String queuelist = queue.toStringHelper(0, queue.size());
			MessageManager.sendEmbedMessage(false, queuelist, channel, null);
		}
	}

}
