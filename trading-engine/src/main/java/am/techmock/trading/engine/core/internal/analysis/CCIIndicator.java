package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;

public class CCIIndicator extends org.ta4j.core.indicators.CCIIndicator implements NamedIndicator {

	public CCIIndicator(BarSeries series, int barCount) {
		super(series, barCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.CCI;
	}
}
