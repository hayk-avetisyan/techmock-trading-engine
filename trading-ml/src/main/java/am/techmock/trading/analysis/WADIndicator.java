package am.techmock.trading.analysis;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

/**
 * Williams' Accumulation/Distribution indicator
 */
public class WADIndicator extends CachedIndicator<Num> {

	private static final Num ZERO = DecimalNum.valueOf(0);

	public WADIndicator(BarSeries series) {
		super(series);
	}

	/**
	 * Definitions:
	 *
	 *      TRH - True Range High
	 *      TRL - True Range Low
	 *
	 *      CCP - Current Closing Price
	 *
	 *      CAD - current Accumulation/Distribution
	 *      WAD - Williams' Accumulation/Distribution
	 *
	 *      PCP - previous closing price
	 *
	 * Calculation:
	 *
	 *      TRH = max(Previous Closing Price, Current High Price)
	 *      TRL = min(Previous Closing Price, Current Low Price)
	 *
	 *      if CCP > PCP
	 *          CAD = CCP - TRL
	 *
	 *      if CCP < PCP
	 *          CAD = CCP - TRH
	 *
	 *      if CCP = PCP
	 *          CAD = 0
	 *
	 *      WAD(i) = CAD + WAD(i-1)
	 *
	 * @param i current index
	 * @return Williams' Accumulation/Distribution
	 */
	@Override
	protected Num calculate(int i) {

		try {

			BarSeries series = getBarSeries();
			Bar current = series.getBar(i);
			Bar last = series.getBar(i - 1);

			Num LCP = last.getClosePrice();

			Num CH = current.getHighPrice();
			Num CL = current.getLowPrice();
			Num CCP = current.getClosePrice();

			Num TRH = CH.max(LCP);
			Num TRL = CL.min(LCP);

			Num CAD = ZERO;

			if (CCP.isGreaterThan(LCP)) {
				CAD = CCP.minus(TRL);
			}
			else if (CCP.isLessThan(LCP)) {
				CAD = CCP.minus(TRH);
			}

			Num lastWAD = getValue(i - 1);

			return CAD.plus(lastWAD);

		} catch (IndexOutOfBoundsException ignore) {}

		return ZERO;
	}
}
