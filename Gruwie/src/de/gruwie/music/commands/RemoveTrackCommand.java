package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.RemoveTrackHelper;
import de.gruwie.util.CheckTrack;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.CheckTrackDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveTrackCommand implements ServerCommand {

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		if(args.length > 1) {
			
			StringBuilder b = new StringBuilder("");
			for(int i = 1; i < args.length; i++) {
				b.append(args[i].toLowerCase() + " ");
			}
			
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			List<CheckTrackDTO> track_list = CheckTrack.getAudioTrack(queue.getQueueList(), b.toString());
			if(track_list != null) {
				if(track_list.size() == 1) queue.removeTrack(track_list.get(0).getTrack());
				else RemoveTrackHelper.multipleFound(track_list, channel);
			}
		}
		else MessageManager.sendEmbedMessage("**YOU HAVE TO PROVIDE A QUERY IN ORDER TO DELETE A TRACK**", channel, true);
	}

}
