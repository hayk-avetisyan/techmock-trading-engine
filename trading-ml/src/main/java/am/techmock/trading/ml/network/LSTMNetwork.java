package am.techmock.trading.ml.network;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LSTMNetwork {

	private static final int hiddenLayerNum = 100;

	public static MultiLayerNetwork buildNetwork(int nIn, int nOut, int batchSize) {

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(System.currentTimeMillis())
				.trainingWorkspaceMode(WorkspaceMode.ENABLED)
				.inferenceWorkspaceMode(WorkspaceMode.ENABLED)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.weightInit(WeightInit.XAVIER)
				.updater(new Nadam(1e-4))
				.miniBatch(batchSize > 1)
				.list()
				.layer(0, new LSTM.Builder()
						.nIn(nIn)
						.nOut(hiddenLayerNum)
						.activation(Activation.TANH)
						.gateActivationFunction(Activation.SIGMOID)
						.dropOut(0.2)
						.build())
				.layer(1, new LSTM.Builder()
						.nIn(hiddenLayerNum)
						.nOut(hiddenLayerNum)
						.activation(Activation.TANH)
						.gateActivationFunction(Activation.SIGMOID)
						.dropOut(0.2)
						.build())
				.layer(2, new LSTM.Builder()
						.nIn(hiddenLayerNum)
						.nOut(hiddenLayerNum)
						.activation(Activation.TANH)
						.gateActivationFunction(Activation.SIGMOID)
						.dropOut(0.2)
						.build())
				.layer(3, new DenseLayer.Builder()
						.nIn(hiddenLayerNum)
						.nOut(hiddenLayerNum)
						.activation(Activation.RELU)
						.build())
				.layer(4, new DenseLayer.Builder()
						.nIn(hiddenLayerNum)
						.nOut(hiddenLayerNum)
						.activation(Activation.RELU)
						.build())
				.layer(5, new OutputLayer.Builder()
						.nIn(hiddenLayerNum)
						.nOut(nOut)
						.activation(Activation.RELU)
						.lossFunction(LossFunctions.LossFunction.MSE)
						.build())
				.build();


		MultiLayerNetwork network = new MultiLayerNetwork(conf);
		network.init();
		return network;
	}
}
