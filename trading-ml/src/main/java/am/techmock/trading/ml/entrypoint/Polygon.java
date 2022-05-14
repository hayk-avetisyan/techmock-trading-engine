package am.techmock.trading.ml.entrypoint;

import am.techmock.trading.analysis.IndicatorType;
import am.techmock.trading.ml.network.DataNormalizer;
import am.techmock.trading.ml.network.representation.TradingDataIterator;
import am.techmock.trading.ml.network.representation.TradingDataProvider;
import am.techmock.trading.ml.tools.CommonFileTools;
import org.apache.commons.io.FilenameUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.jfree.data.xy.XYSeries;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.ta4j.core.BarSeries;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Polygon {

	public static void predict(
			String network, String dataset,
			List<IndicatorType> indicatorTypes, String outputDirectory
	) throws IOException {

		BarSeries series = CommonFileTools.loadSeries(dataset);
		MultiLayerNetwork net = MultiLayerNetwork.load(new File(network), true);
		TradingDataProvider dataProvider = new TradingDataProvider(series);

		TradingDataIterator dataIterator = new TradingDataIterator(dataProvider, indicatorTypes,1,1);
		DataNormalizer normalizer = new DataNormalizer();
		normalizer.fit(dataIterator);

		DataSet dataSet;
		INDArray prediction;
		final XYSeries actual = new XYSeries("Actual");
		final XYSeries predictions = new XYSeries("Prediction");

		int seriesIndex = 0;
		while (dataIterator.hasNext()) {
			dataSet = dataIterator.next();

			prediction = normalizer.revertLabels(net.output(dataSet.getFeatures()));
			predictions.add(seriesIndex, prediction.getDouble(0, 0));
			actual.add(seriesIndex, normalizer.revertLabels(dataSet.getLabels()).getDouble(0, 0));

			seriesIndex++;
		}

		TradingDataChart.drawChart(
				title(outputDirectory, network),
				actual,
				predictions
		);
	}

	public static String title(String outputDirectory, String network) {
		network = FilenameUtils.getBaseName(network);
		return outputDirectory + "/prediction-"+ network  +".png";
	}
}
