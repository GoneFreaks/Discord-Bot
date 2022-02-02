package de.gruwie.commands.admin;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.db.da.TrackDA;
import de.gruwie.util.Formatter;
import de.gruwie.util.Interceptor;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class MetadataCommand implements AdminCommand{

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		
		for (int i = 0; i < 3; i++) {
			try {
				int a = 0/0;
				System.out.println(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int guild_count = PlaylistDA.getPlaylistCount(false);
		int user_count = PlaylistDA.getPlaylistCount(true);
		int track_count = TrackDA.getTrackCount();
		
		String output = "Database:\n\tGuild_Playlist_Count: " + guild_count
				+ "\n\tUser_Playlist_Count: " + user_count
				+ "\n\tTrack_Count: " + track_count
				+ "\n\nCommands:\n\tCommands_Count: " + Gruwie_Startup.INSTANCE.getCmdMan().size()
				+ "\n\tAdminCommands_Count: " + Gruwie_Startup.INSTANCE.getACmdMan().size()
				+ "\n\nExceptions:\n\tSince_Startup: " + Interceptor.counter
				+ "\n\tOccured: ";
		
		StringBuilder b = new StringBuilder(output);
		for (String i : Interceptor.exceptions) {
			b.append("\n\t\t\t[" + i + "]");
		}
		
		b.append("\n\nGeneral:\n\tOnline since: " + Formatter.getDateTime(Gruwie_Startup.start_time));
		b.append("\n\tOnline_Time: " + Formatter.formatTime(System.currentTimeMillis() - Gruwie_Startup.start_time));
		
		MessageManager.sendEmbedPrivateMessage(privateChannel, "```" + b.toString() + "```");
		
	}

}
