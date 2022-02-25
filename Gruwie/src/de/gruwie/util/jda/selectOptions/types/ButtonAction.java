package de.gruwie.util.jda.selectOptions.types;

import java.util.UUID;

import net.dv8tion.jda.api.interactions.components.Button;

public interface ButtonAction {

	public void perform();
	public Button getButton();
	public UUID getUUID();
	
}
