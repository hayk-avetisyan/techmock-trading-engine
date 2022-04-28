package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

/**
 * Stochastic momentum indicator
 */
public class SMIndicator extends CachedIndicator<Num> {

	private ClosePriceIndicator closePrices;
	private StochasticPriceIndicator stochasticPrices;


	SMIndicator(StochasticPriceIndicator stochasticPrices) {
		super(stochasticPrices);
		this.stochasticPrices = stochasticPrices;
		this.closePrices = stochasticPrices.getClosePrices();
	}


	@Override
	protected Num calculate(int index) {
		return this.closePrices.getValue(index).minus(
				this.stochasticPrices.getValue(index)
		);
	}
}
