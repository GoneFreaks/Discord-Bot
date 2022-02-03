package de.gruwie.util.selectOptions;

import java.util.UUID;

import de.gruwie.util.SelectionMenuManager;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class SelectOptionAction extends SelectOption {
	
	public SelectOptionAction(String label) {
		super(label, SelectionMenuManager.getUUID().toString());
	}
	
	public void perform () {
		System.err.println("SelectOptionAction-Dummy-Method");
	}
	
	public UUID getUUID() {
		return UUID.fromString(super.getValue());
	}

}
