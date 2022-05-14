package am.techmock.trading.analysis;

public enum IndicatorType {

	ZeroLine("Zero Line"),
	HighPrice("High price"),
	LowPrice("Low price"),
	HLD("High/Low price difference"),

	OpenPrice("Open price"),
	ClosePrice("Close price"),
	OCD("Open/Close price difference"),

	MACD("Moving Average Convergence/Divergence"),
	CCI("Commodity Channel Index"),
	ATR("Average True Range"),
	BBW("Bollinger Bands Width"),
	EMA("Exponential Moving Average"),
	ROC("Rate of Change"),
	SMI("Stochastic Momentum Index"),
	WR("Williams %R"),
	WAD("Williams' Accumulation/Distribution"),

	SMA("Simple Moving Average"),
	SSMA("Short Simple Moving Average"),
	LSMA("Long Simple Moving Average"),
	SMAD("Simple Moving Average Difference"),

	Momentum("Momentum"),
	SM("Short Momentum"),
	LM("Long Momentum"),
	MD("Momentum Difference");

	private final String name;

	IndicatorType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
