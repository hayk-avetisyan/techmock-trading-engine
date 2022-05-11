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
	private NamedIndicator closePrice;

	public TradingDataProvider(BarSeries series) {
		this.closePrice = new ClosePriceIndicator(series);

		NamedIndicator MACD = new MACDIndicator(closePrice, 12, 26);
		NamedIndicator CCI = new CCIIndicator(series, 20);
		NamedIndicator ATR = new ATRIndicator(series, 14);
		NamedIndicator BBW = new BBWIndicator(closePrice, 20);
		NamedIndicator EMA = new EMAIndicator(closePrice, 20);
		NamedIndicator SMAD = new SMADIndicator(closePrice, 12, 30);
		NamedIndicator MD = new MDIndicator(closePrice, 12, 26);
		NamedIndicator ROC = new ROCIndicator(closePrice, 14);
		NamedIndicator SMI = new SMIIndicator(closePrice);
		NamedIndicator WR = new WilliamsRIndicator(series, 14);
		NamedIndicator OCD = new OCDIndicator(series);
		NamedIndicator HLD = new HLDIndicator(series);

		this.indicators = Arrays.asList(MACD, CCI, ATR, BBW, EMA, SMAD, MD, ROC, SMI, WR, OCD, HLD);
	}

	public double data(int index, int featureIndex) {
		return indicators.get(featureIndex).getValue(index).doubleValue();
	}

	public Pair<IndicatorType, Double> metadata(int index, int featureIndex) {
		return new Pair<>(this.indicators.get(featureIndex).getType(), data(index, featureIndex));
	}

	public double closePrice(int index) {
		return this.closePrice.getValue(index).doubleValue();
	}

	public int featureSize() {
		return indicators.size();
	}

	public BarSeries getSeries() {
		return closePrice.getBarSeries();
	}
}
