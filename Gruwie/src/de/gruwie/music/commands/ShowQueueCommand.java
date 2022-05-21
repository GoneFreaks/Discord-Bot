package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowQueueCommand extends ServerCommand {

	public ShowQueueCommand() {
		super(false, true, ShowQueueCommand.class, Outputs.SHORT_DESCRIPTION_SHOWQUEUE, Outputs.DESCRIPTION_SHOWQUEUE);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			Queue queue = controller.getQueue();
			StringBuilder queuelist;
			int custom_character_count = ConfigManager.getInteger("queue_character_count");
			while(true) {
				queuelist = queue.toStringHelper(0, queue.size(), custom_character_count);
				if(queuelist.length() < 4096) break;
				else custom_character_count -= 2;
			}
			MessageManager.sendEmbedMessageVariable(false, queuelist.toString(), channel.getGuild().getIdLong());
		}
	}

}
