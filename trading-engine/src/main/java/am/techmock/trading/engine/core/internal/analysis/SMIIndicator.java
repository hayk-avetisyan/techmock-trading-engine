package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class SMIIndicator extends AbstractNamedIndicator {

	private static Num HUNDRED = DecimalNum.valueOf(100);

	private SSMIndicator SSM;
	private EMAIndicator denominators;

	public SMIIndicator(Indicator<Num> indicator, int q, int r, int s, int u) {

		super(indicator, IndicatorType.SMI);
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
