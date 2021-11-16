package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeUpCommand extends CommandInfo {

	public VolumeUpCommand() {
		super(false, true, VolumeUpCommand.class, "🔊", "Increase player-volume", "Increase player-volume by 25 per step (max. 100)");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		int new_volume = player.getVolume() + 25;
		player.setVolume((int) Math.min(100, new_volume));
	}
	
}
