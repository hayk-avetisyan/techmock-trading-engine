package am.techmock.trading.ml.tools;

import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
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

	private static final DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final boolean READ_PIP = false;

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
				date = readDate(data);
				open = readValue(data, 1);
				high = readValue(data, 2);
				low = readValue(data, 3);
				close = readValue(data, 5);
				volume = readVolume(data);
				series.addBar(new BaseBar(Duration.ofDays(1), date, open, high, low, close, volume, DecimalNum.valueOf(0)));
			}

			return series;

		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String resolveFilepath(String filepath, boolean exists) {

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

	private static ZonedDateTime readDate(List<Writable> data) {
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

}
