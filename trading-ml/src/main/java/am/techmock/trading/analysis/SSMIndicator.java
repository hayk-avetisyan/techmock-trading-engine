package am.techmock.trading.analysis;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.num.Num;

/**
 * Smoothed stochastic momentum indicator
 */
public class SSMIndicator extends CachedIndicator<Num> {

	private EMAIndicator SSM;

	SSMIndicator(SMIndicator SM, int r, int s, int u) {
		super(SM);

		this.SSM = new EMAIndicator(
				new EMAIndicator(new EMAIndicator(SM, r), s), u
		);
	}

	@Override
	protected Num calculate(int index) {
		return this.SSM.getValue(index);
	}
}
