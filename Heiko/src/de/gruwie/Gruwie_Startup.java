package de.gruwie;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.heiko.db.GetDataBaseConnection;
import de.heiko.listener.CommandListener;
import de.heiko.music.PlayerManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Gruwie_Startup {

	public static Gruwie_Startup INSTANCE;
	private CommandManager cmdMan;
	private ShardManager shardMan;
	private AudioPlayerManager audioPlayerManager;
	private PlayerManager playerManager;

	public static void main(String[] args) {
		if (args.length == 1) {
			
			if(!GetDataBaseConnection.createConnection()) {
				try {
					new Gruwie_Startup().startup(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else System.out.println("Please provide a token");
	}

	public void startup(String token) throws Exception {

		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setActivity(Activity.listening("Rick Astley"));
		builder.setStatus(OnlineStatus.ONLINE);
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.cmdMan = new CommandManager();
		builder.addEventListeners(new CommandListener());
		this.shardMan = builder.build();
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
						System.out.println("Bot offline \n");
						break;
					} else
						System.out.println("Use 'exit' to shutdown");
				}

			} catch (Exception e) {
				e.printStackTrace();
				shardMan.setStatus(OnlineStatus.OFFLINE);
				shardMan.shutdown();
			}
		}).start();
	}

	public CommandManager getCmdMan() {
		return this.cmdMan;
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
