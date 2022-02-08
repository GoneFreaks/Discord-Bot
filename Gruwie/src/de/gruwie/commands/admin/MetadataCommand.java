package de.gruwie.commands.admin;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.sun.management.OperatingSystemMXBean;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.db.da.TrackDA;
import de.gruwie.music.FilterManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.streams.ErrInterceptor;
import de.gruwie.util.streams.OutInterceptor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class MetadataCommand implements AdminCommand {

	public static Thread thread = null;
	private static Message view = null;
	private static OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	
	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		
		if(ConfigManager.getDatabase()) {
			if(thread == null) {
				thread = new Thread(() -> {
					while(!Thread.currentThread().isInterrupted()) {
						try {
							TimeUnit.SECONDS.sleep(2);
							this.performAdminCommand(message, privateChannel);
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
				});
				thread.setName("Metadata");
				thread.start();
			}
			
			if(view != null && !Thread.currentThread().equals(thread)) return;
			
			int guild_count = PlaylistDA.getPlaylistCount(false);
			int user_count = PlaylistDA.getPlaylistCount(true);
			int track_count = TrackDA.getTrackCount();
			
			StringBuilder b = new StringBuilder("");
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
			
			b.append("\n\nGeneral:\n\tOnline since: " + Formatter.getDateTime(Gruwie_Startup.start_time));
			b.append("\n\tOnline_Time: " + Formatter.formatTime(System.currentTimeMillis() - Gruwie_Startup.start_time));
			int filter_count = FilterManager.filterCount();
			b.append("\n\tLoaded_Filters: " + (filter_count == -1? "Not yet loaded" : filter_count));
			int custom_filter_count = FilterManager.customFilterCount();
			b.append("\n\tCustom_Filters: " + (custom_filter_count == -1? "Not yet loaded" : custom_filter_count));
			
			b.append("\n\nJVM-Memory: ");
			b.append("\n\tMaximum: " + Formatter.formatByteSize(Runtime.getRuntime().maxMemory()));
			b.append("\n\tTotal: " + Formatter.formatByteSize(Runtime.getRuntime().totalMemory()));
			b.append("\n\tFree: " + Formatter.formatByteSize(Runtime.getRuntime().freeMemory()));
			b.append("\n\tUsed: " + Formatter.formatByteSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
			double freePercentage = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + 0.0) / Runtime.getRuntime().totalMemory();
			b.append("\n\tUsed(%): " + Formatter.formatDouble(freePercentage * 100) + "%");
		
			b.append("\n\nSystem: ");
			b.append("\n\tCPU:");
			b.append("\n\t\tUsage: " + Formatter.formatDouble(bean.getCpuLoad() * 100) + "%");
			b.append("\n\t\tUsage(Gruwie): " + Formatter.formatDouble(bean.getProcessCpuLoad() * 100) + "%");
			
			b.append("\n\tMemory");
			b.append("\n\t\tTotal: " + Formatter.formatByteSize(bean.getTotalMemorySize()));
			b.append("\n\t\tFree: " + Formatter.formatByteSize(bean.getFreeMemorySize()));
			long used = bean.getTotalMemorySize() - bean.getFreeMemorySize();
			b.append("\n\t\tUsed: " + Formatter.formatByteSize(used));
			b.append("\n\t\tUsed(%): " + Formatter.formatDouble(((used + 0.0) / bean.getTotalMemorySize()) * 100) + "%");
			
			b.append("\n\nThreads: ");
			b.append("\n\tCount: " + Thread.activeCount());
			
			b.append("\n\nCMD-Output: ");
			b.append("\n\n" + OutInterceptor.getCmdOutput());
			
			
			if(view == null) view = MessageManager.sendEmbedPrivateMessage(privateChannel, "```" + b.toString() + "```");
			else MessageManager.editMessage(view, "```" + b.toString() + "```");
		}
		else MessageManager.sendEmbedPrivateMessage(privateChannel, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**");
	}

	public static void interrupt() {
		if(thread != null) thread.interrupt();
	}

}
