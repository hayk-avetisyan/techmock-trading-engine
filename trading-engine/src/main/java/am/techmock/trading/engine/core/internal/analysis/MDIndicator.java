package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Momentum difference Indicator
 */
public class MDIndicator extends AbstractNamedDifferenceIndicator {

	public MDIndicator(Indicator<Num> indicator, int shortBarCount, int longBarCount) {
		super(
				new MomentumIndicator(indicator, shortBarCount, IndicatorType.Momentum),
				new MomentumIndicator(indicator, longBarCount, IndicatorType.Momentum),
				IndicatorType.MD
		);
	}
}
