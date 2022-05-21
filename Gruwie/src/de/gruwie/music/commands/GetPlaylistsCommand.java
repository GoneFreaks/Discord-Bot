package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Dropdown;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPlaylistsCommand extends ServerCommand {

	public GetPlaylistsCommand() {
		super(false, true, GetPlaylistsCommand.class, null, Outputs.OPTIONAL_PARAMETERS_GETPLAYLISTS, Outputs.SHORT_DESCRIPTION_GETPLAYLISTS, Outputs.DESCRIPTION_GETPLAYLISTS);
	}

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) {
		
		String[] args = message.getContentRaw().split(" ");
		
		if(ConfigManager.getDatabase()) {
			if(args.length == 1) Dropdown.getPlaylists(channel, member, true);
			if(args.length == 2) {
				try {
					int count = Integer.parseInt(args[1]);
					if(count <= ConfigManager.getInteger("max_queue_size")) PlaylistManager.playPlaylist(member, channel, PlaylistDA.readRandom(count), "Random[" + count + "]");
					else MessageManager.sendEmbedMessage(true, Outputs.INVALID_PARAMETERS, channel.getGuild().getIdLong());
				} catch (Exception e) {}
			}
		}
		else MessageManager.sendEmbedMessage(true, Outputs.DATABASE, channel);
	}
}
