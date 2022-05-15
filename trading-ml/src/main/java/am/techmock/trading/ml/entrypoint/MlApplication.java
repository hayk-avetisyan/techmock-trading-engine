package am.techmock.trading.ml.entrypoint;


import am.techmock.trading.analysis.IndicatorType;
import am.techmock.trading.ml.tools.CommonFileTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MlApplication {

	private static final String INDICATORS = "-i";
	private static final String PREDICT = "-p";
	private static final String TRAIN = "-t";

	public static void main(String[] args) throws IOException {

		checkGreaterThan(args.length,1);

		if(check(PREDICT, args[0])) {
			checkGreaterThan(args.length, 4);

			String network = CommonFileTools.allowedToExist(args[1], true);
			String dataset = CommonFileTools.allowedToExist(args[2], true);
			String outputDirectory = CommonFileTools.allowedToExist(args[3], true);

			List<IndicatorType> indicatorTypes = new ArrayList<>();
			parseIndicators(args, 4, indicatorTypes);


			Polygon.predict(network, dataset, indicatorTypes, outputDirectory);

		}
		else if(check(TRAIN, args[0])) {

			checkGreaterThan(args.length, 3);

			String network = CommonFileTools.allowedToExist(args[1], false);
			String dataset = CommonFileTools.allowedToExist(args[2], true);

			List<IndicatorType> indicatorTypes = new ArrayList<>();
			parseIndicators(args, 3, indicatorTypes);

			PrepareNetwork.prepare(network, dataset, indicatorTypes);
		}

		else if(check(INDICATORS, args[0])) {
			checkGreaterThan(args.length, 5);

			int startIndex = readInt(args[1]);
			int length = readInt(args[2]);

			String dataset = CommonFileTools.allowedToExist(args[3], true);
			String output = CommonFileTools.allowedToExist(args[4], true);

			List<List<IndicatorType>> indicatorTypes = new ArrayList<>();
			List<IndicatorType> types;
			String[] perChart;

			try {

				for (int i = 5; i < args.length; i++) {

					perChart = args[i].trim().split("-");
					types = new ArrayList<>();

					for (String indicator : perChart) {
						types.add(IndicatorType.valueOf(indicator));
					}

					indicatorTypes.add(types);
				}

			} catch (IllegalArgumentException e) {

				String[] message = e.getMessage().split("\\.");
				System.out.println("Invalid indicator: "+ message[message.length - 1]);
				return;
			}

			TradingDataChart.indicators(dataset, startIndex, length, output, indicatorTypes);
		}
	}

	private static void usage() {

		System.out.println("[usage] trading-ml -t <neural network> <dataset> <indicators>");
		System.out.println("[usage] trading-ml -p <neural network> <dataset> <output directory> <indicators>");
		System.out.println("[usage] trading-ml -i <start index> <length> <dataset> <output directory> <indicators>");
		System.out.println("[usage]");
		System.out.println("[usage]     -t      Trains a new neural network");
		System.out.println("[usage]             and saves it in the given file");
		System.out.println("[usage]");
		System.out.println("[usage]     -p      Loads the neural network using the given file");
		System.out.println("[usage]             and predicts data for the given dataset");
		System.out.println("[usage]");
		System.out.println("[usage]     -i      Prints the given indicators via chart");
		System.out.println("[usage]             and saves them in the given directory as images");
		System.out.println("[usage]");
		System.out.println("[usage]             Available indicators");
		System.out.println("[usage]");

		IndicatorType[] types = IndicatorType.values();

		for (IndicatorType type : types) {
			System.out.println("[usage]                 " + type.toString() + " - " + type.getName());
		}
	}
	private static void parseIndicators(String[] args, int indicatorIndex, List<IndicatorType> indicatorTypes) {
		try {

			for (int i = indicatorIndex; i < args.length; i++) {
				indicatorTypes.add(IndicatorType.valueOf(args[i]));
			}

		} catch (IllegalArgumentException e) {

			String[] message = e.getMessage().split("\\.");
			System.out.println("Invalid indicator used: "+ message[message.length - 1]);
			System.exit(0);
		}
	}

	private static int readInt(String arg) {
		return Integer.parseInt(arg.trim());
	}

	private static boolean check(String flag, String arg) {
		return flag.equals(arg.trim());
	}

	private static void checkGreaterThan(int length, int expected) {
		if(length <= expected) {
			usage();
			System.exit(0);
		}
	}

}
