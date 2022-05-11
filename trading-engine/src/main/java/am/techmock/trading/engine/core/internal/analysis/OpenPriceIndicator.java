package am.techmock.trading.engine.core.internal.analysis;


import org.ta4j.core.BarSeries;

/**
 * Open price indicator
 */
public class OpenPriceIndicator extends org.ta4j.core.indicators.helpers.OpenPriceIndicator implements NamedIndicator {

	public OpenPriceIndicator(BarSeries series) {
		super(series);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.OpenPrice;
	}
}
