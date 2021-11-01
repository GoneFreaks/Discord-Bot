package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportCurrentCommand extends CommandInfo {

	public ExportCurrentCommand() {
		super(false, true, ExportCurrentCommand.class.getSimpleName(), null, "Export current track", "Export the currently played track.\nSongs which have been exported will appear in the random-playlist");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getBoolean("database")) {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			Queue queue = controller.getQueue();
			if(TrackDA.writeTrack(queue.getCurrentTrack().getInfo().uri)) MessageManager.sendEmbedMessage("**TRACK HAS BEEN SAVED**", channel, true);
			else MessageManager.sendEmbedMessage("**TRACK HAS NOT BEEN SAVED, PROBABLY IT'S ALREADY HAVE BEEN SAVED**", channel, true);
		}
		else MessageManager.sendEmbedMessage("**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, true); 
	}
}
