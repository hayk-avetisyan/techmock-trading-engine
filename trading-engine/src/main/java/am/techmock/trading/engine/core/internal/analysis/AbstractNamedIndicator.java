package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

public abstract class AbstractNamedIndicator extends CachedIndicator<Num> implements NamedIndicator {

	private IndicatorType type;

	protected AbstractNamedIndicator(BarSeries series, IndicatorType type) {
		super(series);
		this.type = type;
	}

	protected AbstractNamedIndicator(Indicator<Num> indicator, IndicatorType type) {
		this(indicator.getBarSeries(), type);
	}

	@Override
	public IndicatorType getType() {
		return type;
	}
}
