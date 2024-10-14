package ua.jarvis.data.loader.facade;

import ua.jarvis.data.loader.core.model.User;

import java.util.List;

public interface ConvertorFacade {
	List<User> convert(List<String> lines);
}
