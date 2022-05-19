package am.techmock.trading.ml.entrypoint;

import am.techmock.trading.analysis.IndicatorType;
import am.techmock.trading.ml.network.DataNormalizer;
import am.techmock.trading.ml.network.LSTMNetwork;
import am.techmock.trading.ml.network.representation.TradingDataIterator;
import am.techmock.trading.ml.network.representation.TradingDataProvider;
import am.techmock.trading.ml.tools.CommonFileTools;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PrepareNetwork {

	public static final int EPOCHS = 1000;
	public static final int BATCH_SIZE = 100;
	public static final int PREDICTION_STEP = 1;

	public static void prepare(String networkDirectory, String dataset, List<IndicatorType> indicatorTypes) throws IOException {

		TradingDataProvider provider = new TradingDataProvider(CommonFileTools.loadSeries(dataset));

		TradingDataIterator iterator = new TradingDataIterator(provider, indicatorTypes, BATCH_SIZE, PREDICTION_STEP);
		DataNormalizer preProcessor = new DataNormalizer();

		preProcessor.fit(iterator);
		iterator.setPreProcessor(preProcessor);

		MultiLayerNetwork net = LSTMNetwork.buildNetwork(iterator.inputColumns(), iterator.totalOutcomes(), BATCH_SIZE);

		System.out.println(net.summary());

		ProgressBar pb = new ProgressBarBuilder()
				.setTaskName("Training").setInitialMax(EPOCHS)
				.setUpdateIntervalMillis(3000).setMaxRenderedLength(100)
				.continuousUpdate().build();

		long  start = System.currentTimeMillis();

		for (int epoch = 1; epoch <= EPOCHS; epoch++) {

			net.fit(iterator);

			iterator.reset();
			net.incrementEpochCount();

			pb.step();
			System.gc();
		}

		pb.close();
		System.out.println("\nTraining finished. Duration: "+ time(System.currentTimeMillis() - start));

		ModelSerializer.writeModel(net, new File(networkDirectory),true);
		System.out.println("Model saved - "+ networkDirectory);
		System.exit(0);
	}

	public static String time(long m) {
		long s = m / 1000;
		return String.format("%02d:%02d:%02d.%03d", s / 60 / 60, s / 60, s % 60, m % 1000);
	}
}
