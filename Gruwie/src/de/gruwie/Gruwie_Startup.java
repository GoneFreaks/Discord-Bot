package de.gruwie;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.gruwie.db.ChannelManager;
import de.gruwie.db.GetDataBaseConnection;
import de.gruwie.listener.CommandListener;
import de.gruwie.listener.EmoteListener;
import de.gruwie.listener.SystemListener;
import de.gruwie.music.PlayerManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Gruwie_Startup {

	public static int configuration;
	
	public static Gruwie_Startup INSTANCE;
	private CommandManager cmdMan;
	private EmoteManager emMan;
	private ShardManager shardMan;
	private AudioPlayerManager audioPlayerManager;
	private PlayerManager playerManager;

	public static void main(String[] args) {
		
		if (args.length == 2) {
			try {
				
				switch (Integer.parseInt(args[1])) {
				case 0: {
					if(!GetDataBaseConnection.createConnection()) {
						ErrorClass.shutdown();
						return;
					}
					System.out.println("0: Bot starting in default-mode");
					configuration = 0;
					break;
				}
				
				case 1: {
					System.out.println("1: Bot starting without database");
					ChannelManager.withDataBase = false;
					configuration = 1;
					break;
				}
				
				case 2: {
					if(!GetDataBaseConnection.createConnection()) {
						ErrorClass.shutdown();
						return;
					}
					System.out.println("2: Bot starting without logging");
					ErrorClass.writeToFile = false;
					configuration = 2;
					break;
				}
				
				case 3: {
					System.out.println("3: Bot starting without database and logging");
					ErrorClass.writeToFile = false;
					ChannelManager.withDataBase = false;
					configuration = 3;
					break;
				}
				
				default:
					System.out.println("Ungültige Startargumente");
					configuration = -1;
					return;
				}
				ChannelManager.startup();
				new Gruwie_Startup().startup(args[0]);
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-STARTUP", "SYSTEM"));
			}
		}
		else System.out.println("Please provide a token and a configuration-type");
	}

	public void startup(String token) throws Exception {

		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setActivity(Activity.listening("-help"));
		builder.setStatus(OnlineStatus.ONLINE);
		builder.addEventListeners(new CommandListener(), new EmoteListener(), new SystemListener());
		this.shardMan = builder.build();
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.cmdMan = new CommandManager();
		this.emMan = new EmoteManager();
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		shutdown();

	}

	public void shutdown() {

		new Thread(() -> {

			String line = "";
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
				while ((line = reader.readLine()) != null) {

					if (line.equalsIgnoreCase("exit")) {
						if (shardMan != null) {
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
						}
						break;
					} else
						System.out.println("Use 'exit' to shutdown");
				}

			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-STARTUP", "SYSTEM"));
			}
		}).start();
	}

	public CommandManager getCmdMan() {
		return this.cmdMan;
	}
	
	public EmoteManager getEmMan() {
		return this.emMan;
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
