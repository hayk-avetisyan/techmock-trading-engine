package am.techmock.trading.engine.ml.network.representation;

import am.techmock.trading.engine.core.internal.analysis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.util.Arrays;
import java.util.List;

public class TradingDataProvider {

	private static final Logger logger = LoggerFactory.getLogger(TradingDataProvider.class);


	private List<Indicator<Num>> indicators;
	private ClosePriceIndicator closePrices;

	public TradingDataProvider(BarSeries series) {
		this.closePrices = new ClosePriceIndicator(series);

		Indicator<Num> MACD = new MACDIndicator(closePrices, 12, 26);
		Indicator<Num> CCI = new CCIIndicator(series, 20);
		Indicator<Num> ATR = new ATRIndicator(series, 14);
		Indicator<Num> BBW = new BBWIndicator(closePrices, 20);
		Indicator<Num> EMA = new EMAIndicator(closePrices, 20);
		Indicator<Num> SMAQ = new SMAQIndicator(closePrices, 12, 26);
		Indicator<Num> MQ = new MQIndicator(closePrices, 12, 26);
		Indicator<Num> ROC = new ROCIndicator(closePrices, 14);
		Indicator<Num> SMI = new SMIIndicator(closePrices);
		Indicator<Num> WR = new WilliamsRIndicator(series, 14);
		Indicator<Num> OCQ = new OCQIndicator(series);
		Indicator<Num> HLQ = new HLQIndicator(series);

		this.indicators = Arrays.asList(MACD, CCI, ATR, BBW, EMA, SMAQ, MQ, ROC, SMI, WR, OCQ, HLQ);
	}

	public double data(int index, int featureIndex) {
		return indicators.get(featureIndex).getValue(index).doubleValue();
	}

	public double closePrice(int index) {
		return this.closePrices.getValue(index).doubleValue();
	}

	public int featureSize() {
		return indicators.size();
	}

	public void calculate() {
		int lastIndex = closePrices.getBarSeries().getEndIndex();

		long start = System.currentTimeMillis();

		for (Indicator<Num> indicator : indicators) {
			indicator.getValue(lastIndex);
		}

		long end = System.currentTimeMillis();

		logger.info("Indicators calculation took {} ms", end - start);


	}
}
