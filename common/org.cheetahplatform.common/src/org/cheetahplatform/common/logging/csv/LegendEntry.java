package org.cheetahplatform.common.logging.csv;

public class LegendEntry {
	private String symbol;

	private String explanation;

	public LegendEntry(String symbol, String explanation) {
		this.symbol = symbol;
		this.explanation = explanation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegendEntry other = (LegendEntry) obj;
		if (explanation == null) {
			if (other.explanation != null)
				return false;
		} else if (!explanation.equals(other.explanation))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((explanation == null) ? 0 : explanation.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}
}
