package de.gruwie.commands.admin;

import java.util.concurrent.TimeUnit;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.db.da.TrackDA;
import de.gruwie.util.Formatter;
import de.gruwie.util.ErrInterceptor;
import de.gruwie.util.MessageManager;
import de.gruwie.util.OutInterceptor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class MetadataCommand implements AdminCommand{

	public static Thread thread = null;
	private Message view;
	private PrivateChannel privateChannel;
	
	public MetadataCommand () {
		this.view = null;
		this.privateChannel = null;
	}
	
	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		
		if(thread == null) {
			this.privateChannel = privateChannel;
			thread = new Thread(() -> {
				while(!Thread.currentThread().isInterrupted()) {
					try {
						TimeUnit.SECONDS.sleep(2);
						this.performAdminCommand(message, this.privateChannel);
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		}
		
		if(this.view != null && !Thread.currentThread().equals(thread)) return;
		
		int guild_count = PlaylistDA.getPlaylistCount(false);
		int user_count = PlaylistDA.getPlaylistCount(true);
		int track_count = TrackDA.getTrackCount();
		
		String output = "Database:\n\tGuild_Playlist_Count: " + guild_count
				+ "\n\tUser_Playlist_Count: " + user_count
				+ "\n\tTrack_Count: " + track_count
				+ "\n\nCommands:\n\tCommands_Count: " + Gruwie_Startup.INSTANCE.getCmdMan().size()
				+ "\n\tShortcuts_Count: " + Gruwie_Startup.INSTANCE.getCmdMan().shortcutCount()
				+ "\n\tAdminCommands_Count: " + Gruwie_Startup.INSTANCE.getACmdMan().size()
				+ "\n\nExceptions:\n\tSince_Startup: " + ErrInterceptor.counter
				+ "\n\tOccured: ";
		
		StringBuilder b = new StringBuilder(output);
		for (String i : ErrInterceptor.exceptions) {
			b.append("\n\t\t\t[" + i + "]");
		}
		
		b.append("\n\nGeneral:\n\tOnline since: " + Formatter.getDateTime(Gruwie_Startup.start_time));
		b.append("\n\tOnline_Time: " + Formatter.formatTime(System.currentTimeMillis() - Gruwie_Startup.start_time));
		b.append("\n\nMemory: ");
		b.append("\n\tMaximum: " + Formatter.formatByteSize(Runtime.getRuntime().maxMemory()));
		b.append("\n\tTotal: " + Formatter.formatByteSize(Runtime.getRuntime().totalMemory()));
		b.append("\n\tFree: " + Formatter.formatByteSize(Runtime.getRuntime().freeMemory()));
		b.append("\n\tUsed: " + Formatter.formatByteSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		
		double freePercentage = (Runtime.getRuntime().freeMemory() + 0.0) / Runtime.getRuntime().totalMemory();
		b.append("\n\tFree(%): " + Formatter.formatDouble(freePercentage * 100) + "%");
		
		b.append("\n\nCMD-Output: ");
		b.append("\n\n" + OutInterceptor.getCmdOutput());
		
		if(this.view == null) this.view = MessageManager.sendEmbedPrivateMessage(privateChannel, "```" + b.toString() + "```");
		else MessageManager.editMessage(this.view, "```" + b.toString() + "```");
	}

	public static void interrupt() {
		if(thread != null) thread.interrupt();
	}

}
