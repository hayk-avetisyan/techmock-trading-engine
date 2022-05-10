package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

/**
 * Simple Moving average quotient indicator
 */
public class SMADIndicator extends NamedDifferenceIndicatorAbstract {

	public SMADIndicator(NamedIndicator indicator, int shortBarCount, int longBarCount) {
		super(
				new SMAIndicator(indicator, shortBarCount),
				new SMAIndicator(indicator, longBarCount),
				IndicatorType.SMAD
		);
	}
}
