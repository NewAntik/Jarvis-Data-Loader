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

	public static List<String> read(final String filePath, Long startLine, final Long batchSize) throws IOException {

		int countReadLines = 0;
		final List<String> lines = new ArrayList<>();

		try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
			String line;

			// Skip lines until we reach the startLine
			for (long i = 1; i < startLine; i++) {
				if (reader.readLine() == null) {
					// If we reach the end of the file before reaching the startLine, exit the loop
					return lines;
				}
			}

			// Read lines until batchSize is reached
			while ((line = reader.readLine()) != null && countReadLines != batchSize) {
				lines.add(line);
				countReadLines++;
			}
		}

		LOG.info("FileReader was stopped at line number: {}", countReadLines + startLine - 1);
		return lines;
	}
}