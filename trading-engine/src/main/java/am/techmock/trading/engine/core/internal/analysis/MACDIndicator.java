package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class MACDIndicator extends org.ta4j.core.indicators.MACDIndicator implements NamedIndicator{

	public MACDIndicator(Indicator<Num> indicator, int shortBarCount, int longBarCount) {
		super(indicator, shortBarCount, longBarCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.MACD;
	}
}
