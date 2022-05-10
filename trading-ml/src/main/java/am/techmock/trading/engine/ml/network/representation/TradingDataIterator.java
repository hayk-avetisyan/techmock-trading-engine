package am.techmock.trading.engine.ml.network.representation;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

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
	private final int featureSize;

	private int seriesIndex;
	private int seriesEndIndex;

	private int predictionStep;

	public TradingDataIterator(TradingDataProvider dataProvider, int windowSize, int predictionStep) {
		this.featureSize = dataProvider.featureSize();
		this.dataProvider = dataProvider;
		this.predictionStep = predictionStep;
		this.windowSize = windowSize;
		this.seriesEndIndex = this.dataProvider.getSeries().getEndIndex() - (this.windowSize + this.predictionStep);
		reset();
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
		return this.seriesIndex <= this.seriesEndIndex;
	}

	@Override
	public DataSet next() {

		INDArray observationArray = Nd4j.create(new int[]{1, this.featureSize, this.windowSize}, 'f');
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

	@Override
	public void reset() {
		this.seriesIndex = this.dataProvider.getSeries().getBeginIndex() + 100;
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
}
