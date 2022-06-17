package de.gruwie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.gruwie.db.ChannelManager;
import de.gruwie.db.ConnectionManager;
import de.gruwie.listener.SystemListener;
import de.gruwie.music.MusicController;
import de.gruwie.music.PlayerManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageHolder;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Gruwie_Startup {
	
	public static Gruwie_Startup INSTANCE;
	public static long start_time;
	
	private CommandManager cmdMan;
	private AdminCommandManager acmdMan;
	private ShardManager shardMan;
	private AudioPlayerManager audioPlayerManager;
	private PlayerManager playerManager;

	public static void main(String[] args) {
		try {
			Filter.setErrStream();
		} catch (FileNotFoundException e) {
			System.err.println("UNABLE TO REDIRECT OUTPUT-STREAMS\nPLEASE CONTACT THE HOSTER OF THIS BOT-INSTANCE");
			return;
		}
		
		System.out.println("Running OS:\t\t" + System.getProperty("os.name"));
		System.out.println("Running Java-Version:\t" + System.getProperty("java.version"));
		GruwieUtilities.printBorderline("=");
		start_time = System.currentTimeMillis();
		if(ConfigManager.startup() && args.length == 1) {
			try {
				if(ConfigManager.getBoolean("database")) {
					if(!ConnectionManager.createConnection()) return;
					else {
						System.out.println("Connected to the Database");
						GruwieUtilities.printBorderline("=");
					}
				}
				ChannelManager.startup();
				new Gruwie_Startup().startup(args[0]);
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else System.out.println("PROBLEM BEIM LADEN DER PROPERTIES-DATEI");
	}

	public void startup(String token) throws Exception {

		shutdownTerminal();
		
		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.addEventListeners(new SystemListener());
		
		this.cmdMan = new CommandManager();
		this.playerManager = new PlayerManager();
		this.acmdMan = new AdminCommandManager();
		this.shardMan = builder.build();

		this.audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);

	}

	private void shutdownTerminal() {
		Thread shutdown = new Thread (() -> {
			String line = "";
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("exit")) {
						shutdown();
						break;
					} else System.out.println("Use 'exit' to shutdown");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		shutdown.setName("Shutdown-Terminal");
		shutdown.setDaemon(true);
		shutdown.start();
	}
	
	public void shutdown() throws Exception{
		if (shardMan != null) {
			List<Guild> guilds = Gruwie_Startup.INSTANCE.getShardMan().getGuilds();
			for (Guild i : guilds) {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(i.getIdLong());
				AudioPlayer player = null;
				if((player = controller.getPlayer()) != null) player.destroy();
			}
			MessageHolder.shutdown();
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
		}
	}

	public CommandManager getCmdMan() {
		return this.cmdMan;
	}
	
	public AdminCommandManager getACmdMan() {
		return this.acmdMan;
	}
	
	public ShardManager getShardMan() {
		return this.shardMan;
	}
	
	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return this.audioPlayerManager;
	}

}
