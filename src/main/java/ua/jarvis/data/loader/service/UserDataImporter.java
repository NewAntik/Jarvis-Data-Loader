package ua.jarvis.data.loader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.facade.ConvertorFacade;

import java.io.IOException;
import java.util.List;

@Component
public class UserDataImporter {
	private static final Logger LOG = LoggerFactory.getLogger(UserDataImporter.class);

	private final ConvertorFacade facade;

	private final UserService userService;

	private final FileService fileService;

	public UserDataImporter(
		final ConvertorFacade facade,
		final UserService userService,
		final FileService fileService
	) {
		this.facade = facade;
		this.userService = userService;
		this.fileService = fileService;
	}

	public void readAndSaveData() throws IOException {
		List<String> lines;
		Long duplicates = 0L;
		while (!(lines = fileService.getLines()).isEmpty()) {
			final List<User> users = facade.convert(lines);
			duplicates += userService.saveUsers(users);

			lines = null;
		}

		LOG.info("{} RNOKPP duplicates were(was) found!", duplicates);
	}
}
