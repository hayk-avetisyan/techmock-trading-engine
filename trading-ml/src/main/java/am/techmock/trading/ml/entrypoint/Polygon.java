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

		TradingDataIterator iterator = new TradingDataIterator(dataProvider, indicatorTypes,1,1);
		DataNormalizer normalizer = new DataNormalizer();

		normalizer.fit(iterator);
		iterator.setPreProcessor(normalizer);

		DataSet dataSet;
		INDArray prediction, expected;
		final XYSeries actual = new XYSeries("Actual");
		final XYSeries predictions = new XYSeries("Prediction");

		int seriesIndex = 0;
		while (iterator.hasNext()) {

			dataSet = iterator.next();
			prediction = net.output(dataSet.getFeatures());
			expected = dataSet.getLabels();

			predictions.add(seriesIndex, prediction.getDouble(0, 0));
			actual.add(seriesIndex, expected.getDouble(0, 0));

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
		return outputDirectory + "/"+ network  +".png";
	}
}
