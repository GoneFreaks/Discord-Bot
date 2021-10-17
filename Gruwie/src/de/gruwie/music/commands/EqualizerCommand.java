package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class EqualizerCommand extends CommandInfo implements ServerCommand {

	private static final float[] BASS = {0.15f, 0.1f, 0.05f, 0.0f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,-0.1f, -0.1f, -0.1f, -0.1f};
	private final EqualizerFactory equalizer;
	
	public EqualizerCommand() {
		super(EqualizerCommand.class.getSimpleName(), null, null);
		this.equalizer = new EqualizerFactory();
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		player.setFrameBufferDuration(500);
		player.setFilterFactory(equalizer);
		for (int i = 0; i < BASS.length; i++) {
			equalizer.setGain(i, BASS[i]);
		}
	}
}
