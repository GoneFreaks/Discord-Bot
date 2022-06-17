package de.gruwie.util.selectOptions;

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
