package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.num.Num;

/**
 * Momentum difference Indicator
 */
public class MDIndicator extends DifferenceIndicator {

	public MDIndicator(Indicator<Num> indicator, int shortBarCount, int longBarCount) {
		super(
				new MomentumIndicator(indicator, shortBarCount),
				new MomentumIndicator(indicator, longBarCount)
		);
	}
}
