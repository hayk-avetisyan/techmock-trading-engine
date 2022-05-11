package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.PreviousValueIndicator;
import org.ta4j.core.num.Num;

public class MomentumIndicator extends AbstractNamedDifferenceIndicator {

	public MomentumIndicator(Indicator<Num> indicator, int barCount, IndicatorType type) {
		super(indicator, new PreviousValueIndicator(indicator, barCount), type);
	}
}