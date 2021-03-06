package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.CheckVoiceState;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand extends ServerCommand {
	
	public StopCommand() {
		super(false, true, StopCommand.class, "⏹️", 1, Outputs.SHORT_DESCRIPTION_STOP, Outputs.DESCRIPTION_STOP);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		AudioManager manager = channel.getGuild().getAudioManager();;
		Queue queue = controller.getQueue();
		
		if(player != null) player.stopTrack();
		queue.clearQueue();
		manager.closeAudioConnection();
	}
}
