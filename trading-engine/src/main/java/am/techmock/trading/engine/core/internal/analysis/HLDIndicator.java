package am.techmock.trading.engine.core.internal.analysis;


import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;

/**
 * High/Low prices' quotient indicator
 */
public class HLDIndicator extends NamedDifferenceIndicatorAbstract {

	public HLDIndicator(BarSeries series) {
		super(new HighPriceIndicator(series), new LowPriceIndicator(series), IndicatorType.HLD);
	}
}
