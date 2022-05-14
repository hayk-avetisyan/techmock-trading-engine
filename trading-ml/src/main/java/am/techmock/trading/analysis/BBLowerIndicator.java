package am.techmock.trading.analysis;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class BBLowerIndicator extends CachedIndicator<Num> {

	private CachedIndicator<Num> bbm;
	private StandardDeviationIndicator standardDeviation;

	BBLowerIndicator(SMAIndicator bbm, StandardDeviationIndicator deviationIndicator) {
		super(bbm.getBarSeries());
		this.standardDeviation = deviationIndicator;
		this.bbm = bbm;
	}

	@Override
	protected Num calculate(int index) {
		return bbm.getValue(index).minus(standardDeviation.getValue(index).multipliedBy(DecimalNum.valueOf(2)));
	}
}
