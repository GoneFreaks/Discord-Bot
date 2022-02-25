package de.gruwie.util.jda.selectOptions.BA;

import de.gruwie.util.jda.selectOptions.types.Confirmation;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;

public class ConfirmAction extends SelectOptionAction {

	private final Confirmation confirmation;
	private final boolean accept;
	
	public ConfirmAction (Confirmation confirmation, boolean accept) {
		super("confirm");
		this.confirmation = confirmation;
		this.accept = accept;
	}
	
	@Override
	public void perform() {
		confirmation.confirm(accept);
	}
	
}
