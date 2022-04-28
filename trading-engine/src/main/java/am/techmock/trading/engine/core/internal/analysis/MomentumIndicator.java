package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.helpers.PreviousValueIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;

public class MomentumIndicator extends DifferenceIndicator {

	public MomentumIndicator(PriceIndicator indicator, int barCount) {
		super(indicator, new PreviousValueIndicator(indicator, barCount));
	}
}