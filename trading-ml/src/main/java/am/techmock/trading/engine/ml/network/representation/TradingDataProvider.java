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
	private NamedIndicator closePrices;

	public TradingDataProvider(BarSeries series) {
		this.closePrices = new ClosePriceIndicator(series);

		NamedIndicator MACD = new MACDIndicator(closePrices, 12, 26);
		NamedIndicator CCI = new CCIIndicator(series, 20);
		NamedIndicator ATR = new ATRIndicator(series, 14);
		NamedIndicator BBW = new BBWIndicator(closePrices, 20);
		NamedIndicator EMA = new EMAIndicator(closePrices, 20);
		NamedIndicator SMAD = new SMADIndicator(closePrices, 12, 26);
		NamedIndicator MD = new MDIndicator(closePrices, 12, 26);
		NamedIndicator ROC = new ROCIndicator(closePrices, 14);
		NamedIndicator SMI = new SMIIndicator(closePrices);
		NamedIndicator WR = new WilliamsRIndicator(series, 14);
		NamedIndicator OCD = new OCDIndicatorAbstract(series);
		NamedIndicator HLD = new HLDIndicator(series);

		this.indicators = Arrays.asList(this.closePrices, MACD, CCI, ATR, BBW, EMA, SMAD, MD, ROC, SMI, WR, OCD, HLD);
	}

	public double data(int index, int featureIndex) {
		return indicators.get(featureIndex).getValue(index).doubleValue();
	}

	public Pair<IndicatorType, Double> metadata(int index, int featureIndex) {
		return new Pair<>(this.indicators.get(featureIndex).getType(), data(index, featureIndex));
	}

	public double closePrice(int index) {
		return this.closePrices.getValue(index).doubleValue();
	}

	public int featureSize() {
		return indicators.size();
	}

	public BarSeries getSeries() {
		return closePrices.getBarSeries();
	}
}
