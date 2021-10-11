package de.gruwie;

import java.util.LinkedHashMap;

import de.gruwie.commands.HelpCommand;
import de.gruwie.commands.SetCommand;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.commands.CreateTTTLobbyCommand;
import de.gruwie.games.commands.DeleteTTTLobbyCommand;
import de.gruwie.music.LyricsCommand;
import de.gruwie.music.commands.ClearQueueCommand;
import de.gruwie.music.commands.ExportGuildPlaylistCommand;
import de.gruwie.music.commands.ExportUserPlaylistCommand;
import de.gruwie.music.commands.FastForwardCommand;
import de.gruwie.music.commands.GetPlaylistsCommand;
import de.gruwie.music.commands.NextCommand;
import de.gruwie.music.commands.PlayCommand;
import de.gruwie.music.commands.RemoveTrackCommand;
import de.gruwie.music.commands.RepeatCommand;
import de.gruwie.music.commands.ResumePauseCommand;
import de.gruwie.music.commands.ShuffleCommand;
import de.gruwie.music.commands.StopCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	private static int openCommandCounter = 0;
	
	private LinkedHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		this.storage = new LinkedHashMap<>();
		
		
		this.storage.put("play", new PlayCommand());
		this.storage.put("p", new PlayCommand());
		
		this.storage.put("getplaylists", new GetPlaylistsCommand());
		this.storage.put("gp", new GetPlaylistsCommand());
		
		this.storage.put("exportuserplaylist", new ExportUserPlaylistCommand());
		this.storage.put("eup", new ExportUserPlaylistCommand());
		
		this.storage.put("exportguildplaylist", new ExportGuildPlaylistCommand());
		this.storage.put("egp", new ExportGuildPlaylistCommand());
		
		this.storage.put("resumepause", new ResumePauseCommand());
		this.storage.put("rp", new ResumePauseCommand());
		
		this.storage.put("stop", new StopCommand());
		this.storage.put("s", new StopCommand());
		
		this.storage.put("next", new NextCommand());
		this.storage.put("n", new NextCommand());
		
		this.storage.put("set", new SetCommand());
		
		this.storage.put("clear", new ClearQueueCommand());
		this.storage.put("c", new ClearQueueCommand());
		
		this.storage.put("remove", new RemoveTrackCommand());
		this.storage.put("r", new RemoveTrackCommand());
		
		this.storage.put("help", new HelpCommand());
		this.storage.put("h", new HelpCommand());
		
		this.storage.put("lyrics", new LyricsCommand());
		this.storage.put("l", new LyricsCommand());
		
		this.storage.put("repeat", new RepeatCommand());
		
		this.storage.put("shuffle", new ShuffleCommand());
		
		this.storage.put("fastforward", new FastForwardCommand());
		this.storage.put("ff", new FastForwardCommand());
		
		
		this.storage.put("tictactoe", new CreateTTTLobbyCommand());
		this.storage.put("ttt", new CreateTTTLobbyCommand());
		
		this.storage.put("tictactoedelete", new DeleteTTTLobbyCommand());
		this.storage.put("tttd", new DeleteTTTLobbyCommand());
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getBoolean("open_command") && openCommandCounter < ConfigManager.getInteger("max_open_command")) {
			if(this.storage.containsKey(cmd)) {
				++openCommandCounter;
				this.storage.get(cmd).performServerCommand(member, channel, message);
				--openCommandCounter;
				return false;
			}
			else return true;
		}
		else return false;
	}
	
	@Override
	public String toString() {
		Object[] arr = storage.keySet().toArray();
		StringBuilder b = new StringBuilder("__**Supported commands**__\n\n");
		for (int i = 0; i < arr.length; i++) {
			
			if((i-1 >= 0) && (storage.get(arr[i]).getClass() == storage.get(arr[i-1]).getClass())) b.append("Shortcut: ");
			else b.append("Command: ");
			b.append("*-" + arr[i] + "*\n");
			
			if(!(i+1 >= arr.length) && (storage.get(arr[i]).getClass() != storage.get(arr[i+1]).getClass())) b.append("\n");
		}
		
		b.append("\n\nMod-Creator:\n<@!690659763998031902>\n<@!690255106272526399>\nHosted by: <@!" + ConfigManager.getString("owner_id") + ">");
		
		return b.toString();
	}
	
}
