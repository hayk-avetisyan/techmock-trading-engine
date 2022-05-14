package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.helpers.PreviousValueIndicator;
import org.ta4j.core.num.Num;

public class MomentumIndicator extends DifferenceIndicator {

	public MomentumIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator, new PreviousValueIndicator(indicator, barCount));
	}
}