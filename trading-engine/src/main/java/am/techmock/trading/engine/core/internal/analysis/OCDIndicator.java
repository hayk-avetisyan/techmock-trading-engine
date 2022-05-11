package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;

/**
 * Open/close price difference indicator
 */
public class OCDIndicator extends AbstractNamedDifferenceIndicator {

	public OCDIndicator(BarSeries series) {
		super(new OpenPriceIndicator(series), new ClosePriceIndicator(series), IndicatorType.OCD);
	}
}
