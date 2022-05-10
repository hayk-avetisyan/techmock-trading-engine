package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;

public class ClosePriceIndicator extends org.ta4j.core.indicators.helpers.ClosePriceIndicator implements NamedIndicator {

	public ClosePriceIndicator(BarSeries series) {
		super(series);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.CLP;
	}
}
