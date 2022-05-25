package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.Outputs;
import de.gruwie.util.View;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowEmotesCommand extends ServerCommand {

	public ShowEmotesCommand() {
		super(true, false, ShowEmotesCommand.class, "↙️", null, 5, null, null, Outputs.SHORT_DESCRIPTION_SHOWEMOTES, Outputs.DESCRIPTION_SHOWEMOTES);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		View view = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong()).getQueue().getView();
		if(view != null) view.addEmotes();
	}
	
}
