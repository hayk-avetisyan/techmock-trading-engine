package am.techmock.trading.ml.entrypoint;

import am.techmock.trading.analysis.IndicatorType;
import am.techmock.trading.ml.network.representation.TradingDataProvider;
import am.techmock.trading.ml.tools.CommonFileTools;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TradingDataChart extends JFrame {

	private static final int IMAGE_HEIGHT = 480;
	private static final Logger logger = LoggerFactory.getLogger(TradingDataChart.class);

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

	public static void indicators(String dataset, int startIndex, int length, String outputDirectory, List<List<IndicatorType>> chartIndicators) {

		TradingDataProvider provider = new TradingDataProvider(CommonFileTools.loadSeries(dataset));

		List<ChartPanel> charts = chartIndicators.stream()
				.map(indicators -> chart(provider, indicators, startIndex, length, outputDirectory))
				.collect(Collectors.toList());

		show(charts.toArray(new ChartPanel[]{}));
	}

	private static void show(ChartPanel ... panels) {
		 new TradingDataChart("Trading data chart", panels);
	}

	public static void drawChart(String destination, XYSeries ... data) throws IOException {

		XYSeriesCollection dataset = new XYSeriesCollection();

		for (XYSeries datum : data) {
			dataset.addSeries(datum);
		}

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Prices", "Time", "Trading Prices", dataset
		);

		ChartUtils.saveChartAsPNG(new File(destination), chart, IMAGE_HEIGHT * 3, IMAGE_HEIGHT);
		show(new ChartPanel(chart));
	}

	public static ChartPanel chart(
			TradingDataProvider provider, List<IndicatorType> indicatorTypes,
			int startIndex, int length, String outputDirectory
	) {

		XYSeries series;
		Indicator<Num> indicator;

		int seriesSize = startIndex + length;
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (IndicatorType indicatorType : indicatorTypes) {

			indicator = provider.indicator(indicatorType);
			series = new XYSeries(indicatorType.getName());

			for (int i = 0, seriesIndex = startIndex; seriesIndex < seriesSize; seriesIndex++, i++) {
				series.add(i, indicator.getValue(seriesIndex).doubleValue());
			}
			dataset.addSeries(series);
		}

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Indicators", "Time", "Trading Indicators", dataset
		);

		String destination = title(indicatorTypes, outputDirectory);

		try {
			ChartUtils.saveChartAsPNG(new File(destination), chart, IMAGE_HEIGHT * 3, IMAGE_HEIGHT);

		} catch (IOException e) {
			logger.warn("Unable to save chart at destination {}", destination);
		}

		return new ChartPanel(chart);
	}

	private static String title(List<IndicatorType> types, String outputDirectory) {

		StringBuilder title = new StringBuilder(outputDirectory + "/indicators");
		for (IndicatorType type : types) {
			title.append("-").append(type);
		}

		return title.append(".png").toString();
	}
}
