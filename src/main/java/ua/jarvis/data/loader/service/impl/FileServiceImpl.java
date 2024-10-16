package ua.jarvis.data.loader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.service.FileService;
import ua.jarvis.data.loader.service.util.FileReader;

import java.io.IOException;
import java.util.List;

@Component
public class FileServiceImpl implements FileService {
	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

	private final String bankPath;

	private final Long batchSize;

	private Long lineNumber = 1L;

	public FileServiceImpl(
		@Value("${bank.full.path}") final String bankPath,
		@Value("${batch.size}") final Long batchSize
	) {
		this.bankPath = bankPath;
		this.batchSize = batchSize;
	}

	@Override
	public List<String> getLines() throws IOException {
		final List<String> lines = FileReader.read(bankPath, lineNumber, batchSize);
		lineNumber += batchSize;
		LOG.info("Was loaded {} lines.", lineNumber);

		return lines;
	}
}
