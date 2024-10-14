package ua.jarvis.data.loader.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileReader {
	private static final Logger LOG = LoggerFactory.getLogger(FileReader.class);

	private FileReader() {
	}

	public static List<String> read(final String filePath, final Long startLine, final Long batchSize) throws IOException{
		if (startLine < 1) {
			throw new IllegalArgumentException("Start line must be 1 or greater");
		}
		int currentLine = 0;

		final List<String> lines = new ArrayList<>();
		try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
			String line;

			while ((line = reader.readLine()) != null && currentLine != batchSize) {
				currentLine++;
				if (currentLine >= startLine) {
					lines.add(line);
				}
			}
		}
		LOG.info("FileReader was stopped at line number:{}" , currentLine);

		return lines;
	}
}
