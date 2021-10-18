package de.gruwie;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.commands.ClearCommand;
import de.gruwie.commands.HelpCommand;
import de.gruwie.commands.SetCommand;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.commands.CreateTTTLobbyCommand;
import de.gruwie.games.commands.DeleteTTTLobbyCommand;
import de.gruwie.music.commands.ClearQueueCommand;
import de.gruwie.music.commands.EarRapeCommand;
import de.gruwie.music.commands.EqualizerCommand;
import de.gruwie.music.commands.ExportGuildPlaylistCommand;
import de.gruwie.music.commands.ExportUserPlaylistCommand;
import de.gruwie.music.commands.FastForwardCommand;
import de.gruwie.music.commands.GetPlaylistsCommand;
import de.gruwie.music.commands.LyricsCommand;
import de.gruwie.music.commands.NextCommand;
import de.gruwie.music.commands.PlayCommand;
import de.gruwie.music.commands.RemoveEqualizerCommand;
import de.gruwie.music.commands.RemoveTrackCommand;
import de.gruwie.music.commands.RepeatCommand;
import de.gruwie.music.commands.ResumePauseCommand;
import de.gruwie.music.commands.ShuffleCommand;
import de.gruwie.music.commands.StopCommand;
import de.gruwie.util.ConfigManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {
	
	private ServerCommand[] commands;
	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		
		ServerCommand[] temp = {
				new PlayCommand(),
				
				new GetPlaylistsCommand(),
				new ExportUserPlaylistCommand(),
				new ExportGuildPlaylistCommand(),
				
				new StopCommand(),
				new ResumePauseCommand(),
				new FastForwardCommand(),
				new NextCommand(),
				new RepeatCommand(),
				new ShuffleCommand(),
				new ClearQueueCommand(),
				
				new RemoveTrackCommand(),
				new LyricsCommand(),
				
				new EarRapeCommand(),
				new EqualizerCommand(),
				new RemoveEqualizerCommand(),
				
				new HelpCommand(),
				new ClearCommand(),
				new SetCommand(),
				
				new CreateTTTLobbyCommand(),
				new DeleteTTTLobbyCommand()
		};
		
		this.commands = temp;
		this.storage = initializeMap();
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) throws Exception {
		
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performServerCommand(member, channel, message);
			return false;
		}
		else return true;
	}
	
	public ConcurrentHashMap<String, ServerCommand> initializeMap() {
		ConcurrentHashMap<String, ServerCommand> result = new ConcurrentHashMap<>();
		for (int i = 0; i < commands.length; i++) {
			result.put(commands[i].getCommand(), commands[i]);
			if(commands[i].getShortcut() != null) result.put(commands[i].getShortcut(), commands[i]);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("__**Supported commands**__\n\n");
		String cmd_symbol = ConfigManager.getString("symbol");
		b.append("**Current command symbol " + cmd_symbol + "**\n\n");
		
		for (int i = 0; i < commands.length; i++) {
			b.append(commands[i] + "\n");
		}
		
		b.append("\n**You can use *" + cmd_symbol + "help <command>* in order to get help for a specific command**\n");
		b.append("\n\nMod-Creator:\n<@!690659763998031902>\n<@!690255106272526399>\nHosted by: <@!" + ConfigManager.getString("owner_id") + ">");
		return b.toString();
	}
	
	public ServerCommand getServerCommand(String cmd) {
		return storage.get(cmd);
	}
	
	public String[] getCommandArray() {
		Set<String> temp1 = storage.keySet();
		Object[] temp2 = temp1.toArray();
		
		String[] result = new String[temp1.size()];
		for (int i = 0; i < temp2.length; i++) {
			result[i] = temp2[i].toString();
		}
		return result;
	}
	
}
