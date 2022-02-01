package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteCurrentTrackCommand extends ServerCommand {

	public DeleteCurrentTrackCommand() {
		super(true, true, DeleteCurrentTrackCommand.class, "Delete the current track", "Remove the track currently playing from the database.\nThis includes all records were the corresponding id is used.");
	}

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(ConfigManager.getDatabase()) {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			Queue queue = controller.getQueue();
			AudioTrack track = queue.getCurrentTrack();
			if(member.hasPermission(Permission.ADMINISTRATOR)) {
				if(track != null && track.getInfo().uri != null) {
					boolean result = TrackDA.deleteCertainTrack(track.getInfo().uri);
					MessageManager.sendEmbedMessage(true, result? "**REMOVED TRACK\n*" + track.getInfo().title + "*\nFROM THE DATABASE**" : "**TRACK WASN'T DELETED, MAYBE IT IS NOT STORED INSIDE THE DATABASE**", channel, null);
				}
				else MessageManager.sendEmbedMessage(true, "**SOMETHING WENT WRONG WHILE REMOVING THE TRACK\nMOST LIKELY THERE IS NO ENTY FOR THIS TRACK**", channel, null);
			}
			else MessageManager.sendEmbedMessage(true, "**YOU DON'T HAVE THE PERMISSION TO USE THIS COMMAND**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
}
