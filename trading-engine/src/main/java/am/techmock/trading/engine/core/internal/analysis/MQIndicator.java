package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.helpers.PriceIndicator;

/**
 * Momentum Quotient Indicator
 */
public class MQIndicator extends QuotientIndicator {

	public MQIndicator(PriceIndicator indicator, int shortBarCount, int longBarCount) {
		super(
				new MomentumIndicator(indicator, shortBarCount),
				new MomentumIndicator(indicator, longBarCount)
		);
	}
}
