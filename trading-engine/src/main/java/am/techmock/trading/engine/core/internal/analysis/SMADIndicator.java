package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.SMAIndicator;

/**
 * Simple Moving average difference indicator
 */
public class SMADIndicator extends AbstractNamedDifferenceIndicator {

	public SMADIndicator(NamedIndicator indicator, int shortBarCount, int longBarCount) {
		super(
				new SMAIndicator(indicator, shortBarCount),
				new SMAIndicator(indicator, longBarCount),
				IndicatorType.SMAD
		);
	}
}
