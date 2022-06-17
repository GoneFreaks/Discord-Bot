package de.gruwie.commands.admin;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.FilterManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.streams.ErrInterceptor;
import de.gruwie.util.streams.OutInterceptor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class MetadataCommand implements AdminCommand {

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		
		if(ConfigManager.getDatabase()) {
			MessageManager.sendEmbedPrivateMessage(privateChannel, getMetadataMessage(), false);
		}
		else MessageManager.sendEmbedPrivateMessage(privateChannel, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", true);
	}
	
	public String getMetadataMessage() {
		int guild_count = PlaylistDA.getPlaylistCount(false);
		int user_count = PlaylistDA.getPlaylistCount(true);
		int track_count = TrackDA.getTrackCount();
		
		StringBuilder b = new StringBuilder("```");
		b.append("Database:\n\tGuild_Playlist_Count: " + guild_count);
		b.append("\n\tUser_Playlist_Count: " + user_count);
		b.append("\n\tTrack_Count: " + track_count);
		
		b.append("\n\nCommands:\n\tCommands_Count: " + Gruwie_Startup.INSTANCE.getCmdMan().size());
		b.append("\n\tShortcuts_Count: " + Gruwie_Startup.INSTANCE.getCmdMan().shortcutCount());
		b.append("\n\tExecuted: " + Gruwie_Startup.INSTANCE.getCmdMan().getCommandCount());
		b.append("\n\n\tAdminCommands_Count: " + Gruwie_Startup.INSTANCE.getACmdMan().size());
		b.append("\n\tExecuted: " + Gruwie_Startup.INSTANCE.getACmdMan().getCommandCount());
		
		b.append("\n\nExceptions:\n\tSince_Startup: " + ErrInterceptor.counter);
		b.append("\n\tOccured: ");
		for (String i : ErrInterceptor.exceptions) {
			b.append("\n\t\t\t[" + i + "]");
		}
		
		b.append("\n\nGeneral:\n\tOnline since: " + GruwieUtilities.getDateTime(Gruwie_Startup.start_time));
		b.append("\n\tOnline_Time: " + GruwieUtilities.formatTime(System.currentTimeMillis() - Gruwie_Startup.start_time));
		int filter_count = FilterManager.filterCount();
		b.append("\n\tLoaded_Filters: " + (filter_count == -1? "Not yet loaded" : filter_count));
		int custom_filter_count = FilterManager.customFilterCount();
		b.append("\n\tCustom_Filters: " + (custom_filter_count == -1? "Not yet loaded" : custom_filter_count));
		
		b.append("\n\nCMD-Output: ");
		b.append("\n\n" + OutInterceptor.getCmdOutput());
		
		b.append("```");
		return b.toString();
	}

}
