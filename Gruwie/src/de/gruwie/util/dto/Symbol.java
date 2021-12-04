package de.gruwie.util.dto;

public class Symbol implements Comparable<Symbol> {

	private final String symbol;
	private final int position;
	
	public Symbol (String symbol, int position) {
		this.symbol = symbol;
		this.position = position;
	}
	
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int compareTo(Symbol o) {
		return position - o.position;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
}
