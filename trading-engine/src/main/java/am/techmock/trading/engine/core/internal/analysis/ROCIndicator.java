package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class ROCIndicator extends org.ta4j.core.indicators.ROCIndicator implements NamedIndicator {

	public ROCIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator, barCount);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.ROC;
	}

}
