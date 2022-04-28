package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

/**
 * Simple Moving average quotient indicator
 */
public class SMAQIndicator extends QuotientIndicator {

	public SMAQIndicator(PriceIndicator indicator, int shortBarCount, int longBarCount) {
		super(
				new SMAIndicator(indicator, shortBarCount),
				new SMAIndicator(indicator, longBarCount)
		);
	}
}
