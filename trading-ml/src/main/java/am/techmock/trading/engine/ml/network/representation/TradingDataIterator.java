package am.techmock.trading.engine.ml.network.representation;

import am.techmock.trading.engine.ml.tools.CommonFileTools;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BarSeries;

import java.util.List;
import java.util.Random;

public class TradingDataIterator implements DataSetIterator {

	private static final Logger logger = LoggerFactory.getLogger(TradingDataIterator.class);

	private static final int PREDICTION_VALUES_SIZE = 1;
	private static final int ZERO_INDEX = 0;

	private final TradingDataProvider dataProvider;
	private final Random generator = new Random();

	/**
	 * Agent observation window size
	 * Defines how much historical data the agent can see:
	 *
	 *  _____________________________________________________
	 * |             | 01.01 | 02.01 | 03.01 | 04.01 | 05.01 |
	 *               |       |_______|_______|_______|_______|
	 * | Indicator 1 |       |                               |
	 * | Indicator 2 |       |             window            |
	 * | ...         |       |                               |
	 * |_____________|_______|_______________________________|
	 *                       |                               |
	 *                       |<---------window size--------->|
	 *
	 */
	private final int windowSize;

	/**
	 * count of agent observations
	 *
	 *                       |<----- window 1 ------>|
	 *  _____________________|_______________________|_______
	 * |             | 01.01 | 02.01 | 03.01 | 04.01 | 05.01 |
	 * | Indicator 1 |       |       |       |       |       |
	 * | Indicator 2 |       |       |       |       |       |
	 * | ...         |       |       |       |       |       |
	 * |_____________|_______|_______|_______|_______|_______|
	 *                               |                       |
	 *                               |<----- window 2 ------>|
	 *
	 */
	private final int epochSize;

	private final int featureSize;

	private int seriesIndex;
	private int seriesEndIndex;
	private int seriesStartIndex;

	private int predictionStep;

	public TradingDataIterator(String filepath, int epochSize, int windowSize, int predictionStep) {
		this.epochSize = epochSize;
		this.windowSize = windowSize;
		this.predictionStep = predictionStep;

		BarSeries series = CommonFileTools.loadSeries(filepath);
		this.dataProvider = new TradingDataProvider(series);
		this.featureSize = this.dataProvider.featureSize();

		this.seriesEndIndex = series.getEndIndex() - (this.windowSize + this.predictionStep);
		this.initIndex();

		this.dataProvider.calculate();
	}

	@Override
	public int inputColumns() {
		return this.featureSize;
	}

	@Override
	public int totalOutcomes() {
		return TradingDataIterator.PREDICTION_VALUES_SIZE;
	}

	@Override
	public boolean hasNext() {
		return this.seriesIndex - this.seriesStartIndex < this.epochSize;
	}

	@Override
	public DataSet next() {

		INDArray observationArray = Nd4j.create(new int[]{1 , this.featureSize, this.windowSize}, 'f');
		INDArray labelArray = Nd4j.create(new int[]{1, PREDICTION_VALUES_SIZE, this.windowSize}, 'f');

		int windowStartOffset = this.seriesIndex;
		int windowEndOffset = windowStartOffset + this.windowSize;

		for (int windowOffset = windowStartOffset; windowOffset < windowEndOffset; windowOffset++) {

			int windowIndex = windowOffset - windowStartOffset;

			for (int featureIndex = ZERO_INDEX; featureIndex < this.featureSize; featureIndex++) {

				observationArray.putScalar(
						new int[]{ZERO_INDEX, featureIndex, windowIndex},
						this.dataProvider.data(windowOffset, featureIndex)
				);
			}
			labelArray.putScalar(new int[]{ZERO_INDEX, ZERO_INDEX, windowIndex},
					this.dataProvider.closePrice(windowOffset + this.predictionStep)
			);
		}
		seriesIndex++;
		return new DataSet(observationArray, labelArray);
	}

	@Override
	public DataSet next(int customBatchSize) {
		throw new UnsupportedOperationException("Not supported");
	}

	public void reset() {
		logger.info("Dataset start index for epoch with size {} is {}", this.epochSize, this.seriesStartIndex);
		this.initIndex();
	}

	@Override
	public int batch() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean resetSupported() {
		return true;
	}

	@Override
	public boolean asyncSupported() {
		return false;
	}

	@Override
	public void setPreProcessor(DataSetPreProcessor preProcessor) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public DataSetPreProcessor getPreProcessor() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public List<String> getLabels() {
		return null;
	}

	private void initIndex() {
		this.seriesStartIndex = this.generator.nextInt(this.seriesEndIndex);
		this.seriesIndex = this.seriesStartIndex;
	}
}
