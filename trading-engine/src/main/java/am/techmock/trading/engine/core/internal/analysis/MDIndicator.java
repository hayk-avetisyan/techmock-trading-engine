package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;
import org.ta4j.core.num.Num;

/**
 * Momentum Quotient Indicator
 */
public class MDIndicator extends NamedDifferenceIndicatorAbstract {

	public MDIndicator(Indicator<Num> indicator, int shortBarCount, int longBarCount) {
		super(
				new MomentumIndicator(indicator, shortBarCount),
				new MomentumIndicator(indicator, longBarCount),
				IndicatorType.MD
		);
	}
}
