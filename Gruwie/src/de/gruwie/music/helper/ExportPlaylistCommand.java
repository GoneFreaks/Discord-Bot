package de.gruwie.music.helper;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.ConfigManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.db.PlaylistManager;
import de.gruwie.music.MusicController;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportPlaylistCommand {

	public static void exportPlaylist(Member member, TextChannel channel, Message message, boolean isUser) throws Exception {
		
		if(ConfigManager.getBoolean("database")) {
			String[] args = message.getContentRaw().split(" ");
			
			if(args.length == 2) {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
				List<AudioTrack> tracks = controller.getQueue().getQueueList();
				if(tracks.size() > 0) {
					if(PlaylistManager.exportPlaylist(tracks, args[1], isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser)) {
						MessageManager.sendEmbedMessage("**A PLAYLIST NAMED " + args[1] + " HAS BEEN CREATED**", channel, true);
					}
					else MessageManager.sendEmbedMessage("**SOMETHING WENT WRONG WHILE SAVING THE PLAYLIST\n--> TRY A DIFFRENT NAME**", channel, true);
				}
				else MessageManager.sendEmbedMessage("**THE QUEUE IS EMPTY, NOTHING TO SAVE**", channel, true);
			}
			else MessageManager.sendEmbedMessage("**YOU HAVE TO PROVIDE A NAME FOR YOU'RE PLAYLIST**", channel, true);
		}
		else MessageManager.sendEmbedMessage("**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, true);
	}
	
}
