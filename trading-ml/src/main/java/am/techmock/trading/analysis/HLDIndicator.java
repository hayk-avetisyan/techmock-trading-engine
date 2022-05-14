package am.techmock.trading.analysis;


import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;

/**
 * High/Low price difference indicator
 */
public class HLDIndicator extends DifferenceIndicator {

	public HLDIndicator(BarSeries series) {
		super(new HighPriceIndicator(series), new LowPriceIndicator(series));
	}
}
