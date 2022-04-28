package am.techmock.trading.engine.ml.network.factory;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LSTMNetworkFactory {


	public static MultiLayerNetwork buildNetwork(int nIn, int nOut) {

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(System.currentTimeMillis())
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.weightInit(WeightInit.XAVIER)
				.updater(Updater.RMSPROP)
				.l2(1e-4)
				.list()
				.layer(0, new LSTM.Builder()
						.nIn(nIn)
						.nOut(256)
						.activation(Activation.TANH)
						.gateActivationFunction(Activation.HARDSIGMOID)
						.dropOut(0.2)
						.build())
				.layer(1, new LSTM.Builder()
						.nIn(256)
						.nOut(256)
						.activation(Activation.TANH)
						.gateActivationFunction(Activation.HARDSIGMOID)
						.dropOut(0.2)
						.build())
				.layer(2, new DenseLayer.Builder()
						.nIn(256)
						.nOut(32)
						.activation(Activation.RELU)
						.build())
				.layer(3, new RnnOutputLayer.Builder()
						.nIn(32)
						.nOut(nOut)
						.activation(Activation.IDENTITY)
						.lossFunction(LossFunctions.LossFunction.MSE)
						.build())
				.backpropType(BackpropType.TruncatedBPTT)
				.tBPTTForwardLength(22)
				.tBPTTBackwardLength(22)
				.build();


		MultiLayerNetwork network = new MultiLayerNetwork(conf);
		network.init();
		return network;
	}
}
