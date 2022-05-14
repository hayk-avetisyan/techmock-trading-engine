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

public class TradingDataIterator implements DataSetIterator {

	private final TradingDataProvider dataProvider;
	private DataSetPreProcessor preProcessor;

	/**
	 * Dataset batch size
	 * Defines how much historical data the agent can see sequentially peer iteration:
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

	private final int featureSize;
	private int predictionStep;

	private int seriesIndex;
	private int seriesEndIndex;

	private TreeSet<IndicatorType> indicatorTypes;


	public TradingDataIterator(TradingDataProvider dataProvider, Collection<IndicatorType> indicatorTypes, int batchSize, int predictionStep) {
		this.batchSize = batchSize;
		this.dataProvider = dataProvider;
		this.predictionStep = predictionStep;
		this.featureSize = indicatorTypes.size();
		this.indicatorTypes = new TreeSet<>(indicatorTypes);
		this.seriesEndIndex = this.dataProvider.getSeries().getEndIndex() - this.predictionStep;
		reset();
	}

	@Override
	public int inputColumns() {
		return this.featureSize;
	}

	@Override
	public int totalOutcomes() {
		return 1;
	}

	@Override
	public boolean hasNext() {
		return this.seriesIndex < this.seriesEndIndex;
	}

	@Override
	public DataSet next(int customBatchSize) {

		int batchSize = Math.min(customBatchSize, this.seriesEndIndex - this.seriesIndex);

		INDArray observationArray = Nd4j.zeros(batchSize, this.featureSize, 1);
		INDArray labelArray = Nd4j.zeros(batchSize, 1);


		for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {

			int featureIndex = 0;
			for (IndicatorType indicatorType : indicatorTypes) {

				observationArray.putScalar(
						batchIndex, featureIndex, 0,
						this.dataProvider.data(indicatorType, this.seriesIndex)
				);
				featureIndex++;
			}

			labelArray.putScalar(
					batchIndex, 0,
					this.dataProvider.data(
							IndicatorType.ClosePrice,
							this.seriesIndex + this.predictionStep
					)
			);
			this.seriesIndex++;
		}

		DataSet dataSet = new DataSet(observationArray, labelArray);

		if(this.preProcessor != null) {
			this.preProcessor.preProcess(dataSet);
		}
		return dataSet;
	}

	@Override
	public DataSet next() {
		return this.next(this.batchSize);
	}

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
