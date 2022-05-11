package am.techmock.trading.engine.core.internal.analysis;


import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Simple Moving Average indicator
 */
public class SMAIndicator extends org.ta4j.core.indicators.SMAIndicator implements NamedIndicator {

	private IndicatorType type;

	public SMAIndicator(Indicator<Num> indicator, int barCount, IndicatorType type) {
		super(indicator, barCount);
		this.type = type;
	}

	@Override
	public IndicatorType getType() {
		return type;
	}
}
