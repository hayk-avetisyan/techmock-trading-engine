package am.techmock.trading.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class BBWIndicator extends CachedIndicator<Num> {

	private static final Num DEVIATION_RATIO = DecimalNum.valueOf(2);

	private SMAIndicator BBM;
	private BBUpperIndicator BBU;
	private BBLowerIndicator BBL;

	public BBWIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator);
		BBM = new SMAIndicator(indicator, barCount);
		StandardDeviationIndicator SD = new StandardDeviationIndicator(BBM, barCount);

		BBL = new BBLowerIndicator(BBM, SD);
		BBU = new BBUpperIndicator(BBM, SD);
	}

	@Override
	protected Num calculate(int index) {
		return this.BBU.getValue(index).minus(this.BBL.getValue(index)).dividedBy(this.BBM.getValue(index)).multipliedBy(DEVIATION_RATIO);
	}
}
