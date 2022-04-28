package am.techmock.trading.engine.ml.network.representation;

import am.techmock.trading.engine.ml.tools.CommonFileTools;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.ta4j.core.BarSeries;

import java.util.LinkedList;
import java.util.List;

public class TradingDataIterator implements DataSetIterator {

	private static final int PREDICTION_VALUES_SIZE = 1;
	private static final int ZERO_INDEX = 0;

	private final TradingDataProvider dataProvider;

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
	private final int batchSize;

	private LinkedList<Integer> batchOffsets;

	private final int featureSize;

	/**
	 * observation window current index in data series
	 */
	private int seriesEndIndex;
	private int seriesStartIndex;

	private int predictionStep;

	public TradingDataIterator(String filepath, int batchSize, int windowSize, int predictionStep) {
		this.batchSize = batchSize;
		this.windowSize = windowSize;
		this.predictionStep = predictionStep;

		BarSeries series = CommonFileTools.loadSeries(filepath);
		this.dataProvider = new TradingDataProvider(series);
		this.featureSize = this.dataProvider.featureSize();

		this.seriesEndIndex = series.getEndIndex();
		this.seriesStartIndex = series.getBeginIndex();

		this.dataProvider.calculate();
		this.initOffsets();
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
		return !this.batchOffsets.isEmpty();
	}

	@Override
	public DataSet next() {
		return next(this.batchSize);
	}

	@Override
	public DataSet next(int customBatchSize) {
		int currentBatchSize = Math.min(customBatchSize, this.batchOffsets.size());

		INDArray observationArray = Nd4j.create(new int[]{currentBatchSize, this.featureSize, this.windowSize}, 'f');
		INDArray labelArray = Nd4j.create(new int[]{currentBatchSize, PREDICTION_VALUES_SIZE, this.windowSize}, 'f');

		for (int batchIndex = 0; batchIndex < currentBatchSize; batchIndex++) {

			int windowStartOffset = this.batchOffsets.removeFirst();
			int windowEndOffset = windowStartOffset + this.windowSize;

			for (int windowOffset = windowStartOffset; windowOffset < windowEndOffset; windowOffset++) {

				int windowIndex = windowOffset - windowStartOffset;

				for (int featureIndex = ZERO_INDEX; featureIndex < this.featureSize; featureIndex++) {

					observationArray.putScalar(
							new int[]{batchIndex, featureIndex, windowIndex},
							this.dataProvider.data(windowOffset, featureIndex)
					);
				}
				labelArray.putScalar(new int[]{batchIndex, ZERO_INDEX, windowIndex},
						this.dataProvider.closePrice(windowOffset + this.predictionStep)
				);
			}
		}
		return new DataSet(observationArray, labelArray);
	}

	public void reset() {
		this.initOffsets();
	}

	@Override
	public int batch() {
		return this.batchSize;
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


	private void initOffsets() {
		this.batchOffsets = new LinkedList<>();
		int interval = this.windowSize + this.predictionStep;
		for (int offset = this.seriesStartIndex; offset < this.seriesEndIndex - interval; offset++) {
			this.batchOffsets.add(offset);
		}
	}
}
