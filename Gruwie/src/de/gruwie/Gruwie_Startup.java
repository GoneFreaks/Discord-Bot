package de.gruwie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.gruwie.db.ChannelManager;
import de.gruwie.db.ConnectionManager;
import de.gruwie.db.management.DatabaseManagement;
import de.gruwie.listener.SystemListener;
import de.gruwie.music.MusicController;
import de.gruwie.music.PlayerManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Gruwie_Startup {
	
	public static Gruwie_Startup INSTANCE;
	public static long start_time;
	
	private CommandManager cmdMan;
	private EmoteManager emMan;
	private AdminCommandManager acmdMan;
	private ShardManager shardMan;
	private AudioPlayerManager audioPlayerManager;
	private PlayerManager playerManager;

	public static void main(String[] args) {
		
		System.out.println("Running OS:\t\t" + System.getProperty("os.name"));
		System.out.println("Running Java-Version:\t" + System.getProperty("java.version"));
		Formatter.printBorderline("=");
		
		start_time = System.currentTimeMillis();
		
		if(ConfigManager.startup()) {																
			
			if(ConfigManager.getBoolean("dbmanagement")) DatabaseManagement.boot();
			else {
				try {
					if(ConfigManager.getBoolean("database")) {
						if(!ConnectionManager.createConnection()) return;
						else {
							System.out.println("Connected to the Database");
							Formatter.printBorderline("=");
						}
					}
					ChannelManager.startup();
					new Gruwie_Startup().startup(ConfigManager.getString("token"));
					
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-STARTUP", "SYSTEM", "SYSTEM"));
				}
			}
		}
		else System.out.println("PROBLEM BEIM LADEN DER PROPERTIES-DATEI");
	}

	public void startup(String token) throws Exception {

		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setActivity(Activity.listening("help"));
		builder.setStatus(OnlineStatus.ONLINE);
		builder.addEventListeners(new SystemListener());
		this.shardMan = builder.build();
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.cmdMan = new CommandManager();
		this.emMan = new EmoteManager();
		this.acmdMan = new AdminCommandManager();
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		shutdownTerminal();

	}

	private void shutdownTerminal() {
		
		new Thread(() -> {

			String line = "";
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
				
				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("exit")) {
						shutdown();
						break;
					} else System.out.println("Use 'exit' to shutdown");
				}
				
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-STARTUP", "SYSTEM", "SYSTEM"));
			}
		}).start();
	}
	
	public void shutdown() {
		if (shardMan != null) {
			
			List<Guild> guilds = shardMan.getGuilds();
			for (Guild i : guilds) {
				MusicController controller = playerManager.getController(i.getIdLong());
				AudioPlayer player = null;
				if((player = controller.getPlayer()) != null) player.destroy();
			}
			
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
		}
	}

	public CommandManager getCmdMan() {
		return this.cmdMan;
	}
	
	public EmoteManager getEmMan() {
		return this.emMan;
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
