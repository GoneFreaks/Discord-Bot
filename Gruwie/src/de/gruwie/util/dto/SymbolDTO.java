package de.gruwie.util.dto;

public class SymbolDTO implements Comparable<SymbolDTO> {

	private final String symbol;
	private final int position;
	private final String cmd;
	
	public SymbolDTO (String symbol, int position, String cmd) {
		this.symbol = symbol;
		this.position = position;
		this.cmd = cmd;
	}
	
	public String getCmd() {
		return cmd;
	}
	
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int compareTo(SymbolDTO o) {
		return position - o.position;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
}
