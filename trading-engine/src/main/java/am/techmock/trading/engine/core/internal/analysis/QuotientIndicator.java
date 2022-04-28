package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Quotient Indicator
 */
public class QuotientIndicator extends CachedIndicator<Num> {

	private final Indicator<Num> first;
	private final Indicator<Num> second;

	public QuotientIndicator(Indicator<Num> first, Indicator<Num> second) {
		super(first);
		this.first = first;
		this.second = second;
	}

	protected Num calculate(int index) {

		Num denominator = this.second.getValue(index);

		return denominator.isZero() ? denominator
				: this.first.getValue(index).dividedBy(denominator);
	}
}
