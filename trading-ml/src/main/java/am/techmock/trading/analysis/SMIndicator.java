package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Stochastic momentum indicator
 */
public class SMIndicator extends CachedIndicator<Num> {

	private Indicator<Num> indicator;
	private StochasticPriceIndicator stochasticPrices;


	SMIndicator(StochasticPriceIndicator stochasticPrices) {
		super(stochasticPrices);
		this.stochasticPrices = stochasticPrices;
		this.indicator = stochasticPrices.getIndicator();
	}

	@Override
	protected Num calculate(int index) {
		return this.indicator.getValue(index).minus(
				this.stochasticPrices.getValue(index)
		);
	}
}
