package am.techmock.trading.analysis;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class AxisIndicator implements Indicator<Num> {

	private final Num value;

	public AxisIndicator(double value) {
		this.value = DecimalNum.valueOf(value);
	}
	@Override
	public Num getValue(int index) {
		return this.value;
	}

	@Override
	public BarSeries getBarSeries() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Num numOf(Number number) {
		return DecimalNum.valueOf(number);
	}
}
