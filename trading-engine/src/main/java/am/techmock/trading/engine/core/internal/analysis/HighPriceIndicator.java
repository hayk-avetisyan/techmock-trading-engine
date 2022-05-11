package am.techmock.trading.engine.core.internal.analysis;


import org.ta4j.core.BarSeries;

/**
 * High price indicator
 */
public class HighPriceIndicator extends org.ta4j.core.indicators.helpers.HighPriceIndicator implements NamedIndicator {

	public HighPriceIndicator(BarSeries series) {
		super(series);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.HighPrice;
	}
}
