package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

/**
 * Stochastic Momentum Index
 */
public class SMIIndicator extends CachedIndicator<Num> {

	private static Num HUNDRED = DecimalNum.valueOf(100);

	private SSMIndicator SSM;
	private EMAIndicator denominators;

	public SMIIndicator(Indicator<Num> indicator, int q, int r, int s, int u) {

		super(indicator);
		StochasticPriceIndicator stochasticPrices = new StochasticPriceIndicator(indicator, q);

		this.SSM = new SSMIndicator(new SMIndicator(stochasticPrices), r, s, u);
		this.denominators = new EMAIndicator(
				new EMAIndicator(new EMAIndicator(stochasticPrices, r), s), u
		);
	}

	public SMIIndicator(Indicator<Num> indicator) {
		this(indicator, 5, 20, 5, 3);
	}

	@Override
	protected Num calculate(int index) {

		Num denominator = this.denominators.getValue(index);
		if(denominator.isZero()) return denominator;

		return HUNDRED.multipliedBy(SSM.getValue(index)).dividedBy(denominator);
	}

}
