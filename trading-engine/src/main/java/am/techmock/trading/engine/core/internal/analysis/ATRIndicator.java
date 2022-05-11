package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;

public class ATRIndicator extends org.ta4j.core.indicators.ATRIndicator implements NamedIndicator {

	public ATRIndicator(BarSeries series, int barCount) {
		super(series, barCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.ATR;
	}
}
