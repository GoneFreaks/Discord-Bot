package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.GruwieIO;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Threadpool;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class LyricsCommand extends ServerCommand {
	
	public LyricsCommand() {
		super(true, true, LyricsCommand.class, "Get Lyrics for tracks", "By just using the command itself Gruwie will try to get the lyrics for the track currently playing\nBy using *-command <interpret> - <title>* or *-command <title> - <interpret>* you can get the lyrics for the specific track");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String query;
		String[] args = message.getContentRaw().split(" ");
		
		if(args.length != 1) {
			String symbol = ConfigManager.getString("symbol");
			query = message.getContentRaw().replaceAll(symbol + "lyrics", "").replaceAll(symbol + "l", "").replaceAll(" ", "");
		}
		else {
			MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
			Queue queue = controller.getQueue();
			query = queue.getCurrentTrack() == null? null : queue.getCurrentTrack().getInfo().title;
		}
		
		if(query != null) {
			String[] uri = Formatter.getURL(query);
			if(uri != null) {
				List<Future<String>> futures = new ArrayList<>();
				for (int i = 0; i < uri.length; i++) futures.add(Threadpool.submit(new DoWebBrowsing(uri[i])));
				Future<String> future = Threadpool.submit(new Loop(futures));
				new Thread(() -> {
					try {
						String result = future.get();
						if(result.length() > 0) MessageManager.sendEmbedMessage(false, result, channel, "Powered by: www.azlyrics.com");
						else MessageManager.sendEmbedMessage(true, "**Unable to find lyrics**", channel, null);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}).start();
			}
			
		}
	}
	
}

class Loop implements Callable<String> {
	
	private final List<Future<String>> futures;
	private List<Integer> done;
	
	public Loop (List<Future<String>> futures) {
		this.futures = futures;
		this.done = new ArrayList<>();
	}

	@Override
	public String call() throws Exception {
		while(true) {
			for (int i = 0; i < futures.size(); i++) {
				if(done.size() == futures.size()) return "";
				if(done.contains(i)) continue;
				if(futures.get(i).isDone()) {
					done.add(i);
					if(futures.get(i).get().length() > 0) return futures.get(i).get();
				}
			}
		}
	}
}

class DoWebBrowsing implements Callable<String> {

	private final String uri;
	
	public DoWebBrowsing (String uri) {
		this.uri = uri;
	}

	@Override
	public String call() throws Exception {
		String result = GruwieIO.doWebBrowsing(uri);
		if(result == null) return "";
		else return Formatter.formatWebsite(result);
	}
	
}
