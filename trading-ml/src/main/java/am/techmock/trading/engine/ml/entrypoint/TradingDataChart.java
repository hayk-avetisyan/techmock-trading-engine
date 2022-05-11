package am.techmock.trading.engine.ml.entrypoint;

import am.techmock.trading.engine.core.internal.analysis.IndicatorType;
import am.techmock.trading.engine.ml.network.representation.TradingDataProvider;
import am.techmock.trading.engine.ml.tools.CommonFileTools;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.nd4j.common.primitives.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TradingDataChart extends JFrame {

	public static void main(String[] args) {

		int startIndex = 0;
		int length = 500;
		TradingDataProvider provider = new TradingDataProvider(CommonFileTools.loadSeries("EURUSD.csv"));

		ChartPanel[] charts = new ChartPanel[]{
				constructChart(provider, Arrays.asList(IndicatorType.ClosePrice, IndicatorType.EMA), startIndex, length),
				constructChart(provider, Arrays.asList(IndicatorType.HighPrice, IndicatorType.LowPrice, IndicatorType.OpenPrice, IndicatorType.ClosePrice), startIndex, length),
				constructChart(provider, Arrays.asList(IndicatorType.ClosePrice, IndicatorType.MomentumLong, IndicatorType.MomentumShort), startIndex, length),
				constructChart(provider, Arrays.asList(IndicatorType.ClosePrice, IndicatorType.SMALong, IndicatorType.SMAShort), startIndex, length),
				constructChart(provider, Arrays.asList(IndicatorType.ClosePrice, IndicatorType.BBW), startIndex, length),
				constructChart(provider, IndicatorType.SMI, startIndex, length),
				constructChart(provider, IndicatorType.BBW, startIndex, length),
				constructChart(provider, IndicatorType.ATR, startIndex, length),
				constructChart(provider, IndicatorType.MACD, startIndex, length),
				constructChart(provider, IndicatorType.WR, startIndex, length),
				constructChart(provider, IndicatorType.ROC, startIndex, length),
		};

		show(charts);
	}

	private TradingDataChart(String title, Container ... charts) throws HeadlessException {
		super(title);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(charts.length, 1, 1, 1));
		for (Container chart : charts) {
			panel.add(chart);
		}

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setContentPane(scrollPane);

		pack();
		setVisible(true);
	}

	private static void show(ChartPanel ... panels) {
		 new TradingDataChart("Trading data chart", panels);
	}

	public static ChartPanel constructChart(TradingDataProvider provider, List<IndicatorType> indicators, int startIndex, int length) {

		Pair<IndicatorType, Double> metadata;
		int seriesSize = startIndex + length;
		int indicatorSize = provider.featureSize();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int seriesIndex = startIndex; seriesIndex < seriesSize; seriesIndex++) {
			for (int indicatorIndex = 0; indicatorIndex < indicatorSize; indicatorIndex++) {
				metadata = provider.metadata(seriesIndex, indicatorIndex);

				if(indicators.contains(metadata.getKey())) {
					dataset.addValue(metadata.getValue(), metadata.getKey(), String.valueOf(seriesIndex));
				}
			}
		}

		JFreeChart chart = ChartFactory.createLineChart("Indicators", "Time", "Trading Indicators", dataset);
		return new ChartPanel(chart);
	}

	public static ChartPanel constructChart(TradingDataProvider provider, IndicatorType indicator, int startIndex, int length) {
		return constructChart(provider, Collections.singletonList(indicator), startIndex, length);
	}
}
