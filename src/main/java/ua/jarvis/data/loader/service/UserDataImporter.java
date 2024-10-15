package ua.jarvis.data.loader.service;

import org.springframework.stereotype.Component;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.facade.ConvertorFacade;

import java.io.IOException;
import java.util.List;

@Component
public class UserDataImporter {

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

	//todo check why it starts from 3rd line.
	public void readAndSaveData() throws IOException {
		List<String> lines;
		while((lines = fileService.getLines()) != null){
			final List<User> users = facade.convert(lines);
			userService.saveUsers(users);
			lines = null;
		}
	}
}
