package de.gruwie.commands;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SymbolHelpCommand extends ServerCommand {

	public SymbolHelpCommand() {
		super(false, true, SymbolHelpCommand.class, "❔", 10, "Help for symbols", "Get help for all symbol-commands, e.g. the symbols beneath the queue");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		StringBuilder b = new StringBuilder("");
		for (ServerCommand i : Gruwie_Startup.INSTANCE.getCmdMan().getCommandsWithSymbol()) {
			b.append(i.getSymbol() + "\t" + i.getCommand() + "\n" + i.getDescription() + "\n\n");
		}
		MessageManager.sendEmbedMessage(false, b.toString(), channel, 3, null);
	}
	
}
