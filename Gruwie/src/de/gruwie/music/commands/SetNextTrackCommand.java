package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.CheckTrack;
import de.gruwie.util.Dropdown;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.dto.CheckTrackDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetNextTrackCommand extends ServerCommand {

	public SetNextTrackCommand() {
		super(false, true, SetNextTrackCommand.class, Outputs.PARAMETERS_QUERY, null, Outputs.SHORT_DESCRIPTION_SETNEXTTRACK, Outputs.DESCRIPTION_SETNEXTTRACK);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		String[] args = message.getContentStripped().split(" ");
		
		if(args.length > 1) {
			StringBuilder b = new StringBuilder("");
			for(int i = 1; i < args.length; i++) {
				b.append(args[i].toLowerCase() + " ");
			}
				
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			List<CheckTrackDTO> track_list = CheckTrack.getAudioTrack(queue.getQueueList(), b.toString());
			if(track_list != null) {
				if(track_list.size() == 1) {
					String title = track_list.get(0).getTrack().getInfo().title;
					MessageManager.sendEmbedMessageVariable(true, "**NEXT TRACK:\n" + title + "**", channel.getGuild().getIdLong());
					queue.setNextTrack(title);
				}
				else Dropdown.multipleEntriesFound("\n\n**Which track should be played next?**", track_list, channel, member, true);
			}
		}
	}
	
}
