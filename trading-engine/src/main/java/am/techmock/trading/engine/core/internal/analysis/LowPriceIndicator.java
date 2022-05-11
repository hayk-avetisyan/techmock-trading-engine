package am.techmock.trading.engine.core.internal.analysis;


import org.ta4j.core.BarSeries;

/**
 * Low price indicator
 */
public class LowPriceIndicator extends org.ta4j.core.indicators.helpers.LowPriceIndicator implements NamedIndicator {

	public LowPriceIndicator(BarSeries series) {
		super(series);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.LowPrice;
	}
}
