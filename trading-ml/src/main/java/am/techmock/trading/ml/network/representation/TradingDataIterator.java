package am.techmock.trading.ml.network.representation;

import am.techmock.trading.analysis.IndicatorType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * {@link DataSetIterator} to iterate over trading time series data
 */
public class TradingDataIterator implements DataSetIterator {

	private final TradingDataProvider dataProvider;

	/** {@link DataSetPreProcessor}, used to normalize the data before providing to the agent */
	private DataSetPreProcessor preProcessor;

	/**
	 * Dataset batch size
	 * Defines how much historical data is provided to the agent peer iteration:
	 *
	 *  _____________________________________________________
	 * |             | 01.01 | 02.01 | 03.01 | 04.01 | 05.01 |
	 *               |       |_______|_______|_______|_______|
	 * | Indicator 1 |       |                               |
	 * | Indicator 2 |       |             batch             |
	 * | ...         |       |                               |
	 * |_____________|_______|_______________________________|
	 *                       |                               |
	 *                       |<---------batch  size--------->|
	 *
	 */
	private final int batchSize;

	/** Count of used indicators */
	private final int featureSize;

	/** Steps ahead to predict */
	private int predictionStep;

	/** Current time series index */
	private int seriesIndex;

	/** Last available time series index */
	private int seriesEndIndex;

	/**
	 * Indicators selected to be used in prediction
	 *
	 * The {@link TreeSet} data structure is used so that the order of the indicators
	 * does not interfere with the prediction results.
	 *
	 */
	private TreeSet<IndicatorType> indicatorTypes;

	public TradingDataIterator(TradingDataProvider dataProvider, Collection<IndicatorType> indicatorTypes, int batchSize, int predictionStep) {
		this.batchSize = batchSize;
		this.dataProvider = dataProvider;
		this.predictionStep = predictionStep;
		this.featureSize = indicatorTypes.size();
		this.indicatorTypes = new TreeSet<>(indicatorTypes);
		this.seriesEndIndex = this.dataProvider.getSeries().getEndIndex() - this.predictionStep;
		reset();

		StringBuilder indicators = new StringBuilder("\nSelected indicators");
		for (IndicatorType indicatorType : this.indicatorTypes) {
			indicators.append("\n\t").append(indicatorType.getName());
		}

		System.out.println(indicators);
	}

	/** @return agent input data length */
	@Override
	public int inputColumns() {
		return this.featureSize;
	}

	/** @return label count of agent per input */
	@Override
	public int totalOutcomes() {
		return 1;
	}

	public int dataLength() {
		return this.seriesEndIndex - this.seriesIndex;
	}

	@Override
	public boolean hasNext() {
		return this.seriesIndex < this.seriesEndIndex;
	}

	/**
	 *
	 * Input values are calculated from trading price time series
	 * using {@link #indicatorTypes}
	 *
	 * @return {@link DataSet} of the agent input values
	 *      with the length of the given batchSize if available
	 *      starting from {@link #seriesIndex}
	 */
	@Override
	public DataSet next(int customBatchSize) {

		int batchSize = Math.min(customBatchSize, this.seriesEndIndex - this.seriesIndex);

		INDArray observations = Nd4j.zeros(batchSize, this.featureSize, 1);
		INDArray labels = Nd4j.zeros(batchSize, 1);

		for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {

			int featureIndex = 0;
			for (IndicatorType indicatorType : indicatorTypes) {

				observations.putScalar(
						batchIndex, featureIndex, 0,
						this.dataProvider.data(indicatorType, this.seriesIndex)
				);
				featureIndex++;
			}

			labels.putScalar(
					batchIndex, 0,
					this.dataProvider.data(
							IndicatorType.ClosePrice,
							this.seriesIndex + this.predictionStep
					)
			);
			this.seriesIndex++;
		}

		DataSet dataSet = new DataSet(observations, labels);

		if(this.preProcessor != null) {
			this.preProcessor.preProcess(dataSet);
		}
		return dataSet;
	}

	@Override
	public DataSet next() {
		return this.next(this.batchSize);
	}

	/**
	 * Moves the {@link #seriesIndex} to the beginning of the time series
	 *
	 * We're adding 100 to initial index because
	 * the current values of the indicators are based on previous data,
	 * so the smaller the index, the greater the computation error.
	 */
	@Override
	public void reset() {
		this.seriesIndex = this.dataProvider.getSeries().getBeginIndex() + 100;
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
		this.preProcessor = preProcessor;
	}

	@Override
	public DataSetPreProcessor getPreProcessor() {
		return this.preProcessor;
	}

	@Override
	public List<String> getLabels() {
		return null;
	}
}
