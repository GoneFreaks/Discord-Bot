package de.gruwie.music;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.DataManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.GruwieIO;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class LyricsCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String query;
		
		String[] args = message.getContentRaw().split(" ");
		if(args.length != 1) {
			query = message.getContentRaw().replaceAll("-lyrics", "").replaceAll("-l", "").replaceAll(" ", "");
		}
		else {
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			query = queue.getCurrentTrackTitle();
		}
		String[] uri = Formatter.getURL(query);
		
		if(uri == null) {
			MessageManager.sendEmbedMessage("**Unable to find lyrics**", DataManager.getChannel(channel.getGuild().getIdLong()), true);
			return;
		}
		
		for (int i = 0; i < uri.length; i++) {
			String temp = GruwieIO.doWebBrowsing(uri[i]);
			if (temp != null) {
				String output = Formatter.formatWebsite(temp);
				MessageManager.sendEmbedMessage(output, DataManager.getChannel(channel.getGuild().getIdLong()), false);
				return;
			}
		}
		MessageManager.sendEmbedMessage("**Unable to find lyrics**", DataManager.getChannel(channel.getGuild().getIdLong()), true);
	}
}
