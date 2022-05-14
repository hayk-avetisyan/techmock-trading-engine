package am.techmock.trading.ml.network.representation;

import am.techmock.trading.analysis.*;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.Num;

import java.util.HashMap;
import java.util.Map;

public class TradingDataProvider {

	private Map<IndicatorType, Indicator<Num>> indicators;
	private ClosePriceIndicator ClosePrice;

	public TradingDataProvider(BarSeries series) {
		this.ClosePrice = new ClosePriceIndicator(series);
		this.indicators = new HashMap<>();

		this.indicators.put(IndicatorType.ZeroLine, new AxisIndicator(0));
		this.indicators.put(IndicatorType.MACD, new MACDIndicator(ClosePrice, 12, 26));
		this.indicators.put(IndicatorType.CCI, new CCIIndicator(series, 20));
		this.indicators.put(IndicatorType.ATR, new ATRIndicator(series, 14));
		this.indicators.put(IndicatorType.BBW, new BBWIndicator(ClosePrice, 20));
		this.indicators.put(IndicatorType.EMA, new EMAIndicator(ClosePrice, 20));
		this.indicators.put(IndicatorType.ROC, new ROCIndicator(ClosePrice, 14));
		this.indicators.put(IndicatorType.SMI, new SMIIndicator(ClosePrice));
		this.indicators.put(IndicatorType.WR, new WilliamsRIndicator(series, 14));
		this.indicators.put(IndicatorType.WAD, new WADIndicator(series));

		this.indicators.put(IndicatorType.SSMA, new SMAIndicator(ClosePrice, 10));
		this.indicators.put(IndicatorType.LSMA, new SMAIndicator(ClosePrice, 30));
		this.indicators.put(IndicatorType.SMAD, new SMADIndicator(ClosePrice, 10, 30));

		this.indicators.put(IndicatorType.SM, new MomentumIndicator(ClosePrice, 12));
		this.indicators.put(IndicatorType.LM, new MomentumIndicator(ClosePrice, 26));
		this.indicators.put(IndicatorType.MD, new MDIndicator(ClosePrice, 12, 26));

		this.indicators.put(IndicatorType.OCD, new OCDIndicator(series));
		this.indicators.put(IndicatorType.OpenPrice, new OpenPriceIndicator(series));
		this.indicators.put(IndicatorType.ClosePrice, ClosePrice);

		this.indicators.put(IndicatorType.HLD, new HLDIndicator(series));
		this.indicators.put(IndicatorType.HighPrice, new HighPriceIndicator(series));
		this.indicators.put(IndicatorType.LowPrice, new LowPriceIndicator(series));
	}

	public double data(IndicatorType indicatorType, int seriesIndex) {
		return indicators.get(indicatorType).getValue(seriesIndex).doubleValue();
	}

	public Indicator<Num> indicator(IndicatorType indicatorType) {
		return this.indicators.get(indicatorType);
	}

	public BarSeries getSeries() {
		return ClosePrice.getBarSeries();
	}
}
