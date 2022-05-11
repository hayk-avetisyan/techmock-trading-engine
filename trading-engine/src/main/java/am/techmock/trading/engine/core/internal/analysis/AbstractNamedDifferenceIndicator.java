package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.num.Num;

/**
 * Quotient Indicator
 */
public abstract class AbstractNamedDifferenceIndicator extends DifferenceIndicator implements NamedIndicator {

	private IndicatorType type;

	public AbstractNamedDifferenceIndicator(Indicator<Num> first, Indicator<Num> second, IndicatorType type) {
		super(first, second);
		this.type = type;
	}

	@Override
	public IndicatorType getType() {
		return type;
	}
}
