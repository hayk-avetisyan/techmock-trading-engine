package am.techmock.trading.analysis;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;

/**
 * Open/close price difference indicator
 */
public class OCDIndicator extends DifferenceIndicator {

	public OCDIndicator(BarSeries series) {
		super(new OpenPriceIndicator(series), new ClosePriceIndicator(series));
	}
}
