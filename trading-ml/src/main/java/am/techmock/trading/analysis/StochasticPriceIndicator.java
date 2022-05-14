package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class StochasticPriceIndicator extends CachedIndicator<Num> {

	private static final Num CONSTANT_RATIO = DecimalNum.valueOf(0.5);

	private Indicator<Num> indicator;
	private int startIndex;
	private int barCount;

	StochasticPriceIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator);
		this.startIndex = indicator.getBarSeries().getBeginIndex();
		this.indicator = indicator;
		this.barCount = barCount;
	}

	Indicator<Num> getIndicator() {
		return this.indicator;
	}

	/**
	 * Definitions:
	 *  HH          - The highest price
	 *  LL          - The lowest price
	 *  p, barCount - Period
	 *
	 *
	 * Calculation:
	 *  1/2 * [ HH(p) - LL(p) ]
	 *
	 */
	@Override
	protected Num calculate(int index) {
		Num LL, HH, indexPrice;

		HH = LL = indicator.getValue(index);
		int period = Math.min(barCount, index - startIndex);

		for (int i = 1; i < period; i++) {
			indexPrice = indicator.getValue(index - i);
			LL = indexPrice.min(LL);
			HH = indexPrice.max(HH);
		}

		return CONSTANT_RATIO.multipliedBy(LL.plus(HH));
	}
}