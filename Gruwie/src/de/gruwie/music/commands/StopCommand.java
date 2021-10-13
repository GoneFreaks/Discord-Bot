package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand implements ServerCommand{

	private static final String COMMAND = "stop";
	private static final String SHORTCUT = "s";
	private static final String SYMBOL = ":stop_button:";
	private static final String DESCRIPTION = "Gruwie will do the following things: *Stop playing music, Clearing the music-queue, leaving the voice-channel*\nIf noone except Gruwie is connected to a voice-channel this command will be executed automatically";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		AudioManager manager = channel.getGuild().getAudioManager();;
		Queue queue = controller.getQueue();
		
		if(player != null) player.stopTrack();
		queue.clearQueue();
		manager.closeAudioConnection();
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getCommand() {
		return COMMAND;
	}

	@Override
	public String getShortcut() {
		return SHORTCUT;
	}

	@Override
	public String getSymbol() {
		return SYMBOL;
	}
}
