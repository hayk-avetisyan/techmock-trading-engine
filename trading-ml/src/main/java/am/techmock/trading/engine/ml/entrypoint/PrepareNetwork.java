package am.techmock.trading.engine.ml.entrypoint;

import am.techmock.trading.engine.ml.network.factory.LSTMNetworkFactory;
import am.techmock.trading.engine.ml.network.representation.TradingDataIterator;
import am.techmock.trading.engine.ml.network.representation.TradingDataProvider;
import am.techmock.trading.engine.ml.tools.CommonFileTools;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class PrepareNetwork {

	private static final Logger logger = LoggerFactory.getLogger(PrepareNetwork.class);

	public static final int EPOCHS = 100;
	public static final int WINDOW_SIZE = 20;
	public static final int PREDICTION_STEP = 1;

	public static void prepare(String network, String dataset) throws IOException {

		TradingDataProvider provider = new TradingDataProvider(CommonFileTools.loadSeries(dataset));

		TradingDataIterator iterator = new TradingDataIterator(provider, WINDOW_SIZE, PREDICTION_STEP);
		MultiLayerNetwork net = LSTMNetworkFactory.buildNetwork(iterator.inputColumns(), iterator.totalOutcomes(), WINDOW_SIZE, "./");

		long start;
		for (int i = 0; i < EPOCHS; i++) {
			start = System.currentTimeMillis();
			net.fit(iterator);
			net.rnnClearPreviousState();

			logger.info("Epoch: {}, Score: {}, Duration: {}", i + 1, net.score(), time(System.currentTimeMillis() - start));
		}

		File locationToSave = new File(network);
		ModelSerializer.writeModel(net, locationToSave, true);
		logger.info("Model saved");
		System.exit(0);
	}

	public static String time(long m) {
		long s = m / 1000;
		return String.format("%02d:%02d.%03d", s / 60, s % 60, m % 1000);
	}
}
