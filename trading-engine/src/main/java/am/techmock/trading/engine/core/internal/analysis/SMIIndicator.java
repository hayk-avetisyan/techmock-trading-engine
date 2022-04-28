package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class SMIIndicator extends CachedIndicator<Num> {

	private static Num HUNDRED = DecimalNum.valueOf(100);

	private SSMIndicator SSM;
	private EMAIndicator denominators;

	public SMIIndicator(ClosePriceIndicator closePrices, int q, int r, int s, int u) {

		super(closePrices);
		StochasticPriceIndicator stochasticPrices = new StochasticPriceIndicator(closePrices, q);

		this.SSM = new SSMIndicator(new SMIndicator(stochasticPrices), r, s, u);
		this.denominators = new EMAIndicator(
				new EMAIndicator(new EMAIndicator(stochasticPrices, r), s), u
		);
	}

	public SMIIndicator(ClosePriceIndicator closePrices) {
		this(closePrices, 5, 20, 5, 3);
	}

	@Override
	protected Num calculate(int index) {

		Num denominator = this.denominators.getValue(index);
		if(denominator.isZero()) return denominator;

		return HUNDRED.multipliedBy(SSM.getValue(index)).dividedBy(denominator);
	}

}
