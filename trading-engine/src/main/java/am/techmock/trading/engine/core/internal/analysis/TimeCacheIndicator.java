package am.techmock.trading.engine.core.internal.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

public abstract class TimeCacheIndicator extends CachedIndicator<Num> {

	private static final Logger logger = LoggerFactory.getLogger(TimeCacheIndicator.class);

	private String name;

	protected TimeCacheIndicator(BarSeries series, String name) {
		super(series);
		this.name = name;
	}

	protected TimeCacheIndicator(Indicator<?> indicator, String name) {
		super(indicator);
		this.name = name;
	}

	@Override
	public Num getValue(int index) {
		long start = System.currentTimeMillis();
		Num value =  super.getValue(index);
		long end = System.currentTimeMillis();

		logger.info("Indicator {} calculation took {} ms", name, end - start);
		return value;
	}
}
