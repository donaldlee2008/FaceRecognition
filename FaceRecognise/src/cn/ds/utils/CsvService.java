package cn.ds.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CsvService {
	private CsvWriter writer;
	private CsvReader reader;

	public CsvService(String writePath, String readPath) {
		if (writePath != null) {
			writer = new CsvWriter(writePath, ',', Charset.forName("SJIS"));
		}
		if (readPath != null) {
			try {
				reader = new CsvReader(readPath, ',', Charset.forName("SJIS"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String[] contents) {
		try {
			writer.writeRecord(contents);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> read(boolean isReadHeader) {
		List<String[]> contents = new ArrayList<String[]>();
		try {
			if (!isReadHeader) {
				reader.readHeaders();
			}

			while (reader.readRecord()) {
				contents.add(reader.getValues());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}

	public void closeWriter() {
		if (writer != null) {
			try {
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
		}
	}
}
