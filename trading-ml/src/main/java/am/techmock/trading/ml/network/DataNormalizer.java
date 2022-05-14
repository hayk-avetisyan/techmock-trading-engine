package am.techmock.trading.ml.network;

import am.techmock.trading.ml.network.representation.TradingDataIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;

import java.util.*;

public class DataNormalizer implements DataSetPreProcessor {

	private boolean initialized;

	private Map<Integer, Double> featureMax;

	private Map<Integer, Double> featureMin;

	private double labelMin;
	private double labelMax;

	public DataNormalizer() {
		featureMin = new HashMap<>();
		featureMax = new HashMap<>();
	}

	@Override
	public void preProcess(DataSet dataSet) {
		INDArray preProcessedFeatures = preProcessFeatures(dataSet.getFeatures());
		INDArray preProcessedLabels = preProcessLabels(dataSet.getLabels());

		dataSet.setFeatures(preProcessedFeatures);
		dataSet.setLabels(preProcessedLabels);
	}

	public INDArray preProcessFeatures(INDArray features) {
		return changeFeatures(features, (x, min, max) -> (x - min) / (max - min));
	}

	public INDArray preProcessLabels(INDArray features) {
		return changeLabels(features, x -> (x - labelMin) / (labelMax - labelMin));
	}

	public INDArray revertLabels(INDArray features) {
		return changeLabels(features, x -> (x + labelMin) * (labelMax - labelMin));
	}

	public void fit(TradingDataIterator iterator) {

		while (iterator.hasNext()) {
			DataSet dataSet = iterator.next();
			INDArray features = dataSet.getFeatures();
			INDArray labels = dataSet.getLabels();

			if (!initialized) {
				init(dataSet);
			}

			double min, max;
			double currentFeature, currentLabel;

			long[] shape = features.shape();
			long featuresLength = shape[1];
			long batchSize = shape[0];

			for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {
				for (int featureIndex = 0; featureIndex < featuresLength; featureIndex++) {

					min = this.featureMin.get(featureIndex);
					max = this.featureMax.get(featureIndex);

					currentFeature = features.getDouble(batchIndex, featureIndex, 0);

					if (currentFeature > max) {
						featureMax.put(featureIndex, currentFeature);
					} else if (currentFeature < min) {
						featureMin.put(featureIndex, currentFeature);
					}
				}

				currentLabel = labels.getDouble(batchIndex, 0);
				if (currentLabel > labelMax) {
					labelMax = currentLabel;
				}
				else if (currentLabel < labelMin) {
					labelMin = currentLabel;
				}
			}
		}

		iterator.reset();
	}

	private INDArray changeFeatures(INDArray features, Function function) {

		double currentValue;
		double min, max;

		long[] shape = features.shape();
		long featuresLength = shape[1];
		long batchSize = shape[0];

		for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {
			for (int featureIndex = 0; featureIndex < featuresLength; featureIndex++) {

				min = this.featureMin.get(featureIndex);
				max = this.featureMax.get(featureIndex);

				currentValue = features.getDouble(batchIndex, featureIndex, 0);
				features.putScalar(
						batchIndex, featureIndex, 0,
						function.calculate(currentValue, min, max)
				);
			}
		}

		return features;
	}

	private INDArray changeLabels(INDArray labels, java.util.function.Function<Double, Double> function) {

		double currentValue;
		long[] shape = labels.shape();
		long batchSize = shape[0];

		for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {

			currentValue = labels.getDouble(batchIndex,0);
			labels.putScalar(batchIndex, 0, function.apply(currentValue));
		}

		return labels;
	}

	private void init(DataSet dataSet) {
		INDArray features = dataSet.getFeatures();
		INDArray labels = dataSet.getLabels();

		long featuresLength = features.shape()[1];
		labelMin = labelMax = labels.getDouble(0, 0);

		for (int featureIndex = 0; featureIndex < featuresLength; featureIndex++) {
			featureMin.put(featureIndex, features.getDouble(0, featureIndex, 0));
			featureMax.put(featureIndex, features.getDouble(0, featureIndex, 0));
		}
		initialized = true;
	}

	private interface Function {
		double calculate(double value, double min, double max);
	}
}