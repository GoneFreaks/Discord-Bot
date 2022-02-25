package de.gruwie.util.jda.selectOptions.types;

import java.util.UUID;

import de.gruwie.util.jda.SelectionMenuManager;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class SelectOptionAction extends SelectOption {
	
	public SelectOptionAction(String label) {
		super(label, SelectionMenuManager.getUUID().toString());
	}
	
	public void perform () {
		System.err.println("SelectOptionAction-Dummy-Method");
	}
	
	public final UUID getUUID() {
		return UUID.fromString(super.getValue());
	}

}
