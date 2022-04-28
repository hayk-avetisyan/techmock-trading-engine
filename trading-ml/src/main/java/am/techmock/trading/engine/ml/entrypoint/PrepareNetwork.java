package am.techmock.trading.engine.ml.entrypoint;

import am.techmock.trading.engine.ml.network.factory.LSTMNetworkFactory;
import am.techmock.trading.engine.ml.network.representation.TradingDataIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public class PrepareNetwork {

	private static final Logger logger = LoggerFactory.getLogger(PrepareNetwork.class);

	public static final int EPOCHS = 45;
	public static final int EPOCH_SIZE = 200;
	public static final int WINDOW_SIZE = 20;
	public static final int PREDICTION_STEP = 1;

	public static void prepare(String networkPath, String dataPath) throws IOException {

		TradingDataIterator dataIterator = new TradingDataIterator(dataPath, EPOCH_SIZE, WINDOW_SIZE, PREDICTION_STEP);
		MultiLayerNetwork net = LSTMNetworkFactory.buildNetwork(dataIterator.inputColumns(), dataIterator.totalOutcomes());

		long start;
		for (int i = 0; i < EPOCHS; i++) {
			start = System.currentTimeMillis();
			net.fit(dataIterator);

			logger.info("Epoch: {}, Score: {}, Duration: {} ms", i + 1, net.score(),
					System.currentTimeMillis() - start
			);
		}

		File locationToSave = new File(networkPath);
		ModelSerializer.writeModel(net, locationToSave, true);
	}
}
