package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.MultipleEntriesFound;
import de.gruwie.util.CheckTrack;
import de.gruwie.util.dto.CheckTrackDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetNextTrackCommand extends ServerCommand {

	public SetNextTrackCommand() {
		super(true, true, SetNextTrackCommand.class, "Set the next track to play", "Set the next track which will be played\nIf there's already one this won't work");
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
				if(track_list.size() == 1) queue.setNextTrack(track_list.get(0).getTrack().getInfo().title);
				else MultipleEntriesFound.promptDialog("\n\n**Which track should be played next?**", track_list, channel, member, true);
			}
		}
	}
	
}
