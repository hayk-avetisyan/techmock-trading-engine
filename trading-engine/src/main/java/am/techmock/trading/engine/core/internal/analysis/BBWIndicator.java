package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.PriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class BBWIndicator extends AbstractNamedIndicator {

	private static final Num DEVIATION_RATIO = DecimalNum.valueOf(2);

	private SMAIndicator BBM;
	private BBUpperIndicator BBU;
	private BBLowerIndicator BBL;

	public BBWIndicator(Indicator<Num> indicator, int barCount) {
		super(indicator, IndicatorType.BBW);
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
