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
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.Threadpool;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class LyricsCommand extends ServerCommand {
	
	public LyricsCommand() {
		super(false, true, LyricsCommand.class, null, Outputs.OPTIONAL_PARAMETERS_LYRICS, Outputs.SHORT_DESCRIPTION_LYRICS, Outputs.DESCRIPTION_LYRICS);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		String query;
		String[] args = message.getContentRaw().split(" ");
		GruwieUtilities.log("Parameter-Count " + args.length);
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
			String[] url = GruwieUtilities.getURL(query);
			if(url != null) {
				List<Future<String>> futures = new ArrayList<>();
				for (int i = 0; i < url.length; i++) futures.add(Threadpool.submit(new DoWebBrowsing(url[i])));
				Future<String> future = Threadpool.submit(new WaitForBrowsing(futures));
				Threadpool.execute(() -> {
					try {
						String result = future.get();
						if(result.length() > 0) MessageManager.sendEmbedMessageVariable(false, result, channel.getGuild().getIdLong(), Outputs.LYRICS_FOOTER);
						else MessageManager.sendEmbedMessage(true, Outputs.LYRICS, channel);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				});
			}
			else MessageManager.sendEmbedMessage(true, Outputs.LYRICS, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.LYRICS, channel);
	}
	
}

class WaitForBrowsing implements Callable<String> {
	
	private final List<Future<String>> futures;
	private List<Integer> done;
	
	public WaitForBrowsing (List<Future<String>> futures) {
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

	private final String url;
	
	public DoWebBrowsing (String url) {
		this.url = url;
	}

	@Override
	public String call() throws Exception {
		String result = GruwieUtilities.doWebBrowsing(url, "<!-- MxM banner -->");
		if(result == null) return "";
		else return GruwieUtilities.formatWebsite(result);
	}
}
