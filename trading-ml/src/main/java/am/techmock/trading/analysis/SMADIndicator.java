package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.num.Num;

/**
 * Simple Moving average difference indicator
 */
public class SMADIndicator extends DifferenceIndicator {

	public SMADIndicator(Indicator<Num> indicator, int shortBarCount, int longBarCount) {
		super(
				new SMAIndicator(indicator, shortBarCount),
				new SMAIndicator(indicator, longBarCount)
		);
	}
}
