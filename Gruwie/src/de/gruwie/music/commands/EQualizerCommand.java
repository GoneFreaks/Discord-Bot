package de.gruwie.music.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.TrackEqualizer;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class EQualizerCommand extends ServerCommand{

	public EQualizerCommand() {
		super(true, true, EQualizerCommand.class, null, Outputs.OPTIONAL_PARAMETERS_EQUALIZER, Outputs.SHORT_DESCRIPTION_EQUALIZER, Outputs.DESCRIPTION_EQUALIZER);
	}
	
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		String[] args = message.getContentRaw().split(" ");
		GruwieUtilities.log("Parameter-Count " + args.length);
		if(args.length == 1) {
			TrackEqualizer eq = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong()).getEqualizer();
			Message m = MessageManager.sendEmbedMessageVariable(false, eq.toString(), channel.getGuild().getIdLong());
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
