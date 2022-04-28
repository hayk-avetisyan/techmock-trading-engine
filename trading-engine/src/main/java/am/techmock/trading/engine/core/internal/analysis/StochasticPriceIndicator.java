package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class StochasticPriceIndicator extends CachedIndicator<Num> {

	private static final Num CONSTANT_RATIO = DecimalNum.valueOf(0.5);

	private ClosePriceIndicator closePrices;
	private int startIndex;
	private int barCount;

	StochasticPriceIndicator(ClosePriceIndicator closePrices, int barCount) {
		super(closePrices);
		this.startIndex = closePrices.getBarSeries().getBeginIndex();
		this.closePrices = closePrices;
		this.barCount = barCount;
	}

	ClosePriceIndicator getClosePrices() {
		return this.closePrices;
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

		HH = LL = closePrices.getValue(index);
		int period = Math.min(barCount, index - startIndex);

		for (int i = 1; i < period; i++) {
			indexPrice = closePrices.getValue(index - i);
			LL = indexPrice.min(LL);
			HH = indexPrice.max(HH);
		}

		return CONSTANT_RATIO.multipliedBy(LL.plus(HH));
	}
}