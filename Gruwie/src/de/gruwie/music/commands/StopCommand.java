package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ServerCommand{

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		AudioPlayer player = CheckVoiceState.checkVoiceState(member, channel);
		if(player != null) player.stopTrack();
	}

}
