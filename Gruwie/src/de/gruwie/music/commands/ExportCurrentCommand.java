package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportCurrentCommand extends ServerCommand {

	public ExportCurrentCommand() {
		super(true, true, ExportCurrentCommand.class, "Export current track", "Saves the track currently playing and stores it in the database.\nAfter that the track may occur in the **\"random\"** or **\"recommended\"** playlist");
	}

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(ConfigManager.getDatabase()) {
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			String uri = queue.getCurrentTrack().getInfo().uri;
			MessageManager.sendEmbedMessage(true, TrackDA.writeTrack(uri)? "**TRACK HAS BEEN SAVED**" : "**TRACK WASN'T SAVED\nMOST LIKELY THIS HAS ALREADY HAPPENED**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
}
