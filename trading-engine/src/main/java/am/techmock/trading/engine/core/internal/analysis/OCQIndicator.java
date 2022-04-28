package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;

/**
 * Open/close prices' quotient indicator
 */
public class OCQIndicator extends QuotientIndicator {

	public OCQIndicator(BarSeries series) {
		super(new OpenPriceIndicator(series), new ClosePriceIndicator(series));
	}
}
