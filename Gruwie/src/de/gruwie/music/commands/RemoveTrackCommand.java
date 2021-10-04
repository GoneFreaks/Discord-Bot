package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.CheckTrack;
import de.gruwie.util.MessageManager;
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
				b.append(args[i].toLowerCase());
			}
			
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			AudioTrack track = CheckTrack.getAudioTrack(queue.getQueueList(), b.toString());
			
			if(track != null) queue.removeTrack(track);
			else MessageManager.sendEmbedMessage("MULTIPLE RESULTS FOUND", ChannelManager.getChannel(channel), true);
		}
	}

}