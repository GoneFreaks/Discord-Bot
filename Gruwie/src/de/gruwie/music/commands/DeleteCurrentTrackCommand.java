package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.CheckVoiceState;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteCurrentTrackCommand extends ServerCommand {

	public DeleteCurrentTrackCommand() {
		super(true, true, DeleteCurrentTrackCommand.class, Outputs.SHORT_DESCRIPTION_DELETECURRENTTRACK, Outputs.DESCRIPTION_DELETECURRENTTRACK);
	}

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		if(ConfigManager.getDatabase()) {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			Queue queue = controller.getQueue();
			AudioTrack track = queue.getCurrentTrack();
			if(member.hasPermission(Permission.ADMINISTRATOR)) {
				if(track != null && track.getInfo().uri != null) {
					boolean result = TrackDA.deleteCertainTrack(track.getInfo().uri);
					MessageManager.sendEmbedMessageVariable(true, result? "**REMOVED TRACK\n*" + track.getInfo().title + "*\nFROM THE DATABASE**" : "**TRACK WASN'T DELETED, MAYBE IT IS NOT STORED INSIDE THE DATABASE**", channel.getGuild().getIdLong());
					GruwieUtilities.log("removed " + track.getInfo().title + " from the database");
				}
			}
			else MessageManager.sendEmbedMessage(true, Outputs.PERMISSION, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.DATABASE, channel);
	}
	
}
