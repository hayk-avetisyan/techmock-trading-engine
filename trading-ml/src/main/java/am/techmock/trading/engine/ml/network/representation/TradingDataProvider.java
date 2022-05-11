package am.techmock.trading.engine.ml.network.representation;

import am.techmock.trading.engine.core.internal.analysis.*;
import am.techmock.trading.engine.core.internal.analysis.ATRIndicator;
import am.techmock.trading.engine.core.internal.analysis.CCIIndicator;
import am.techmock.trading.engine.core.internal.analysis.EMAIndicator;
import am.techmock.trading.engine.core.internal.analysis.MACDIndicator;
import am.techmock.trading.engine.core.internal.analysis.ROCIndicator;
import am.techmock.trading.engine.core.internal.analysis.WilliamsRIndicator;
import org.nd4j.common.primitives.Pair;
import org.ta4j.core.BarSeries;

import java.util.Arrays;
import java.util.List;

public class TradingDataProvider {

	private List<NamedIndicator> indicators;
	private NamedIndicator ClosePrice;

	public TradingDataProvider(BarSeries series) {
		this.ClosePrice = new ClosePriceIndicator(series);

		NamedIndicator MACD = new MACDIndicator(ClosePrice, 12, 26);
		NamedIndicator CCI = new CCIIndicator(series, 20);
		NamedIndicator ATR = new ATRIndicator(series, 14);
		NamedIndicator BBW = new BBWIndicator(ClosePrice, 20);
		NamedIndicator EMA = new EMAIndicator(ClosePrice, 20);
		NamedIndicator ROC = new ROCIndicator(ClosePrice, 14);
		NamedIndicator SMI = new SMIIndicator(ClosePrice);
		NamedIndicator WR = new WilliamsRIndicator(series, 14);
		NamedIndicator SMAShort = new SMAIndicator(ClosePrice, 12, IndicatorType.SMAShort);
		NamedIndicator SMALong = new SMAIndicator(ClosePrice, 40, IndicatorType.SMALong);
		NamedIndicator MomentumShort = new MomentumIndicator(ClosePrice, 12, IndicatorType.MomentumShort);
		NamedIndicator MomentumLong = new MomentumIndicator(ClosePrice, 26, IndicatorType.MomentumLong);
		NamedIndicator OpenPrice = new OpenPriceIndicator(series);
		NamedIndicator HighPrice = new HighPriceIndicator(series);
		NamedIndicator LowPrice = new LowPriceIndicator(series);

		this.indicators = Arrays.asList(
				MACD, CCI, ATR, BBW, EMA, ROC, SMI, WR,
				SMAShort, SMALong, MomentumShort, MomentumLong, OpenPrice, ClosePrice, HighPrice, LowPrice);
	}

	public double data(int index, int featureIndex) {
		return indicators.get(featureIndex).getValue(index).doubleValue();
	}

	public Pair<IndicatorType, Double> metadata(int index, int featureIndex) {
		return new Pair<>(this.indicators.get(featureIndex).getType(), data(index, featureIndex));
	}

	public double closePrice(int index) {
		return this.ClosePrice.getValue(index).doubleValue();
	}

	public int featureSize() {
		return indicators.size();
	}

	public BarSeries getSeries() {
		return ClosePrice.getBarSeries();
	}
}
