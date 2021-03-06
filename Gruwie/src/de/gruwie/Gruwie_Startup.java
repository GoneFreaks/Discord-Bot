package de.gruwie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.gruwie.db.ChannelManager;
import de.gruwie.db.ConnectionManager;
import de.gruwie.listener.SystemListener;
import de.gruwie.music.PlayerManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageHolder;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.OnlineStatus;
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
		
		GruwieUtilities.logMeta("Running OS:\t\t" + System.getProperty("os.name"));
		GruwieUtilities.logMeta("Running Java-Version:\t" + System.getProperty("java.version"));

		start_time = System.currentTimeMillis();
		if(ConfigManager.startup() && args.length == 1) {
			try {
				if(ConfigManager.getBoolean("database")) {
					if(!ConnectionManager.createConnection()) return;
					else {
						GruwieUtilities.logMeta("Connected to the Database");
					}
				}
				ChannelManager.startup();
				new Gruwie_Startup().startup(args[0]);
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else GruwieUtilities.logMeta("Unable to load the properties file");
	}

	public void startup(String token) throws Exception {

		GruwieUtilities.log();
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
		GruwieUtilities.log();
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
		GruwieUtilities.log();
		if (shardMan != null) {
			Gruwie_Startup.INSTANCE.getPlayerManager().destroyPlayers();
			MessageHolder.shutdown();
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
		}
	}

	public CommandManager getCmdMan() {
		GruwieUtilities.log();
		return this.cmdMan;
	}
	
	public AdminCommandManager getACmdMan() {
		GruwieUtilities.log();
		return this.acmdMan;
	}
	
	public ShardManager getShardMan() {
		GruwieUtilities.log();
		return this.shardMan;
	}
	
	public PlayerManager getPlayerManager() {
		GruwieUtilities.log();
		return this.playerManager;
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		GruwieUtilities.log();
		return this.audioPlayerManager;
	}

}
