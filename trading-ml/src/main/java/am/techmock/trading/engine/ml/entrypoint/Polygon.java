package am.techmock.trading.engine.ml.entrypoint;

import am.techmock.trading.engine.ml.network.representation.TradingDataProvider;
import am.techmock.trading.engine.ml.tools.CommonFileTools;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.ta4j.core.BarSeries;

import java.io.File;
import java.io.IOException;

public class Polygon {

	private static final int WINDOW_SIZE = 20;

	public static void predict(String network, String dataset) throws IOException {


		BarSeries series = CommonFileTools.loadSeries(dataset);
		MultiLayerNetwork net = MultiLayerNetwork.load(new File(network), true);
		TradingDataProvider dataProvider = new TradingDataProvider(series);

		int barCount = series.getBarCount();
		int featureSize = dataProvider.featureSize();

		INDArray observation, prediction;

		for (int seriesIndex = 0; seriesIndex < barCount; seriesIndex++) {

			observation = Nd4j.create(new int[]{1, featureSize, WINDOW_SIZE}, 'f');

			for (int windowIndex = 0; windowIndex < WINDOW_SIZE; windowIndex++) {
				for (int featureIndex = 0; featureIndex < featureSize; featureIndex++) {

					observation.putScalar(
							new int[]{0, featureIndex, windowIndex},
							dataProvider.data(seriesIndex, featureIndex)
					);
				}
			}

			prediction = net.output(observation);
			net.clearLayersStates();
			System.out.println(prediction);
		}


	}
}
