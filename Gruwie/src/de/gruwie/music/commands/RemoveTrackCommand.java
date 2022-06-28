package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.CheckTrack;
import de.gruwie.util.Dropdown;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.dto.CheckTrackDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveTrackCommand extends ServerCommand {
	
	public RemoveTrackCommand() {
		super(false, true, RemoveTrackCommand.class, Outputs.PARAMETERS_QUERY, null, Outputs.SHORT_DESCRIPTION_REMOVETRACK, Outputs.DESCRIPTION_REMOVETRACK);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		String[] args = message.getContentStripped().split(" ");
		GruwieUtilities.log("Parameter-Count " + args.length);
		
		if(args.length > 1) {
			StringBuilder b = new StringBuilder("");
			for(int i = 1; i < args.length; i++) {
				b.append(args[i].toLowerCase() + " ");
			}
			GruwieUtilities.log("Query: " + b.toString());	
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			List<CheckTrackDTO> track_list = CheckTrack.getAudioTrack(queue.getQueueList(), b.toString());
			if(track_list != null) {
				if(track_list.size() == 1) {
					MessageManager.sendEmbedMessageVariable(true, "**REMOVED TRACK:\n" + track_list.get(0).getTrack().getInfo().title + "**", channel.getGuild().getIdLong());
					queue.removeTrack(track_list.get(0).getTrack().getInfo().title);
				}
				else if(track_list.size() <= 5) Dropdown.multipleEntriesFound("\n\n**Which track should be deleted?**", track_list, channel, member, false);
				else MessageManager.sendEmbedMessage(true, Outputs.AMBIGUOUS, channel);
			}
		}
	}
}
