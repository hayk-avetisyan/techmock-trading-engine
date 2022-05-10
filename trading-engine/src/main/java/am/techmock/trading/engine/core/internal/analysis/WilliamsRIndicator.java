package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;

public class WilliamsRIndicator extends org.ta4j.core.indicators.WilliamsRIndicator implements NamedIndicator {


	public WilliamsRIndicator(BarSeries barSeries, int barCount) {
		super(barSeries, barCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.WR;
	}
}
