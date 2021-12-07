package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeDownCommand extends ServerCommand {

	public VolumeDownCommand() {
		super(false, true, VolumeDownCommand.class, "🔉", 8, "Decrease player-volume", "Decrease player-volume by 25 per step (min. 25)");
	}

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		int new_volume = player.getVolume() - 25;
		player.setVolume((int) Math.max(25, new_volume));
	}
	
}
