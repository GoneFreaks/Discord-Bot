package de.gruwie.util.dto;

public class SymbolDTO implements Comparable<SymbolDTO> {

	private final String symbol;
	private final int position;
	
	public SymbolDTO (String symbol, int position) {
		this.symbol = symbol;
		this.position = position;
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
