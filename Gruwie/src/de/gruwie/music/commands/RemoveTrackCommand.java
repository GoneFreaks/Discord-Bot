package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.CommandInfo;
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

public class RemoveTrackCommand extends CommandInfo implements ServerCommand {
	
	public RemoveTrackCommand() {
		super(false, true, RemoveTrackCommand.class.getSimpleName(), null, "In addition to the command itself you have to provide a query, to identify the track you want to remove.\nIf the result is a single track it will be removed immediately, else a dialog shows up with the possible options.\nIf there are more than five results you have to provide a more accurate query");
	}
	
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
