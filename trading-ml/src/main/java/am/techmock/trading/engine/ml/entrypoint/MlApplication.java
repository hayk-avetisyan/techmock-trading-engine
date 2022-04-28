package am.techmock.trading.engine.ml.entrypoint;


import static am.techmock.trading.engine.ml.tools.CommonFileTools.*;
import java.io.IOException;


public class MlApplication {

	private static final String PREDICT_FLAG = "--predict";
	private static final String LEARN_FLAG = "--learn";


	public static void main(String[] args) throws IOException {

		if(args.length != 3) {
			usage();
			return;
		}

		String dataset = resolveFilepath(args, 2,true);
		if(inPredictMode(args)) {

			String network = resolveFilepath(args, 1,true);
			Polygon.predict(network, dataset);
			return;
		}

		else if(inLearnMode(args)) {

			String network = resolveFilepath(args, 1,false);
			PrepareNetwork.prepare(network, dataset);
			return;
		}

		usage();
	}

	public static boolean inPredictMode(String[] args) {
		return PREDICT_FLAG.equals(args[0].trim());
	}

	public static boolean inLearnMode(String[] args) {
		return LEARN_FLAG.equals(args[0].trim());
	}

	private static void usage() {
		System.out.println("[usage] trading-ml [--learn | --predict] <network file> <dataset file>");
		System.out.println("[usage]");
		System.out.println("[usage]    --learn      preparing a new neural network");
		System.out.println("[usage]                 and saving it in the given file");
		System.out.println("[usage]");
		System.out.println("[usage]    --predict    loading the neural network from the given filepath");
		System.out.println("[usage]                 and testing it with the given dataset");
	}

}
