package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class EMAIndicator extends org.ta4j.core.indicators.EMAIndicator implements NamedIndicator {

	public EMAIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator, barCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.EMA;
	}
}
