package de.gruwie.music.commands;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class EarRapeCommand extends ServerCommand {

	public EarRapeCommand() {
		super(false, true, EarRapeCommand.class, "No comment", "Push the player for a short period of time to its limits");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		if(player.getVolume() == 100) return;
		new Thread(() -> {
			try {
				int volume = player.getVolume();
				player.setVolume(100);
				TimeUnit.SECONDS.sleep(5);
				player.setVolume(volume);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}).start();
	}

}
