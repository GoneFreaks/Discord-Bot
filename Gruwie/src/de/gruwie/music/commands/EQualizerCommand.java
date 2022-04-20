package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.TrackEqualizer;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class EQualizerCommand extends ServerCommand{

	public EQualizerCommand() {
		super(true, true, EQualizerCommand.class, null, "Freq-Band between 1 and 15, Gain between -100% and +400%", "Show or modify the current equalizer", "By using this command without any parameters Gruwie will display the current equalizer and its freq-bands.\nYou can modify these bands by providing the optional parameters");
	}
	
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		String[] args = message.getContentRaw().split(" ");
		if(args.length == 1) {
			TrackEqualizer eq = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong()).getEqualizer();
			Message m = MessageManager.sendEmbedMessage(false, eq.toString(), channel, null);
			eq.setMessage(m);
		}
		if(args.length == 3) {
			try {
				int band = Integer.parseInt(args[1]);
				int gain = Integer.parseInt(args[2]);
				
				if(gain <= 400 && gain >= -100 && gain % 50 == 0) {
					
					float gain_float = gain / 400.0f;
					
					TrackEqualizer eq = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong()).getEqualizer();
					eq.setGain(gain_float, band - 1);
				}
			} catch (Exception e) {
				System.out.println("A");
				return;
			}
		}
	}
	
}
