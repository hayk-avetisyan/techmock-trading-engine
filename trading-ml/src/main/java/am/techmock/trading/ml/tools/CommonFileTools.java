package am.techmock.trading.ml.tools;

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
import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class CommonFileTools {

	private static final Logger logger = LoggerFactory.getLogger(CommonFileTools.class);
	private static final DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/** If is true fractional part of currency prices are used as initial data */
	private static final boolean READ_PIP = false;

	/**
	 * Creates and returns a {@link BarSeries} of
	 * trading data using the given csv file
	 */
	public static BarSeries loadSeries(String csv) {
		BaseBarSeries series = new BaseBarSeries();

		try {

			ZonedDateTime date;
			List<Writable> data;
			Num open, high, low, close, volume;

			CSVRecordReader reader = new CSVRecordReader();
			reader.initialize(new FileSplit(new File(csv)));

			while (reader.hasNext()) {
				data = reader.next();
				date = readDate(data);
				open = readValue(data, 1);
				high = readValue(data, 2);
				low = readValue(data, 3);
				close = readValue(data, 5);
				volume = readVolume(data);
				series.addBar(new BaseBar(Duration.ofDays(1), date, open, high, low, close, volume, DecimalNum.valueOf(0)));
			}

		} catch (Exception e) {
			logger.debug(e.getMessage());
			System.out.println("Error occurred while parsing trading data file: "+ csv);
			System.exit(0);
		}

		return series;
	}

	/** Checks if a file is allowed to exist or not */
	public static String allowedToExist(String filepath, boolean exists) {
		filepath = filepath.trim();

		if(fileExists(filepath) != exists) {
			System.out.println("Invalid filepath "+ filepath +": file should" + (exists ? " " : " not ") + "exist.");
			System.exit(0);
		}

		return filepath;
	}

	private static boolean fileExists(String filepath) {
		return new File(filepath).exists();
	}

	/**
	 * Reads and returns the price at the given index of {@link Writable}
	 * @return pip if {@link #READ_PIP} is true and pure price otherwise
	 */
	private static DecimalNum readValue(List<Writable> data, int index) {
		return READ_PIP
				? readPipValue(data, index)
				: readDouble(data, index);
	}

	private static DecimalNum readDouble(List<Writable> data, int index) {
		return DecimalNum.valueOf(data.get(index).toDouble());
	}

	private static DecimalNum readPipValue(List<Writable> data, int index) {
		double price = data.get(index).toDouble();
		int pip = (int) ((price - (int) price) * 10000);
		return DecimalNum.valueOf(pip / 100D);
	}

	private static DecimalNum readVolume(List<Writable> data) {
		return DecimalNum.valueOf(data.get(6).toDouble());
	}

	private static ZonedDateTime readDate(List<Writable> data) {
		String dateString = data.get(0).toString();

		return ZonedDateTime.of(
				LocalDate.parse(dateString, dateFormatter),
				LocalTime.of(0, 0),
				ZoneId.of("America/New_York")
		);
	}

}
