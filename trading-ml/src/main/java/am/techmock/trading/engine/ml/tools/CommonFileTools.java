package am.techmock.trading.engine.ml.tools;

import am.techmock.trading.engine.ml.entrypoint.MlApplication;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class CommonFileTools {

	private static DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final Logger logger = LoggerFactory.getLogger(MlApplication.class);


	public static BarSeries loadSeries(String CSVFilepath) {
		BaseBarSeries series = new BaseBarSeries();

		try {

			ZonedDateTime date;
			List<Writable> data;
			Num open, high, low, close, volume;

			CSVRecordReader reader = new CSVRecordReader();
			reader.initialize(new FileSplit(new File(CSVFilepath)));

			while (reader.hasNext()) {
				data = reader.next();
				date = getDate(data);
				open = readPricePip(data, 1);
				high = readPricePip(data, 2);
				low = readPricePip(data, 3);
				close = readPricePip(data, 4);
				volume = readVolume(data);
				series.addBar(new BaseBar(Duration.ofDays(1), date, open, high, low, close, volume, DecimalNum.valueOf(0)));
			}

			return series;

		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String resolveFilepath(String[] args, int index, boolean exists) {
		String filepath = args[index].trim();

		if(fileExists(filepath) != exists) {
			logger.error("Invalid filepath {}: file should" + (exists ? " " : " not ") + "exist.", filepath, exists);
			System.exit(0);
		}

		return filepath;
	}

	private static boolean fileExists(String filepath) {
		return new File(filepath).exists();
	}


	private static ZonedDateTime getDate(List<Writable> data) {
		String dateString = data.get(0).toString();

		return ZonedDateTime.of(
				LocalDate.parse(dateString, dateFormatter),
				LocalTime.of(0, 0),
				ZoneId.of("America/New_York")
		);
	}

	private static DecimalNum readVolume(List<Writable> data) {
		return DecimalNum.valueOf(data.get(6).toDouble());
	}

	private static DecimalNum readPricePip(List<Writable> data, int index) {
		double price = data.get(index).toDouble();
		double pip = (price - (int) price) * 100;
		return DecimalNum.valueOf(pip);
	}

}
