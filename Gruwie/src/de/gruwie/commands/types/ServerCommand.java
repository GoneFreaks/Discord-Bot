package de.gruwie.commands.types;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand{

	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception;
	
	public String getDescription();
	
	public String getCommand();
	
	public String getShortcut();
	
	public String getSymbol();
	
	public boolean isWip();
}
