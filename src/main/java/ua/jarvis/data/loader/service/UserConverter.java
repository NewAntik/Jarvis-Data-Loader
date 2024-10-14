package ua.jarvis.data.loader.service;

import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.core.model.enums.ConvertorType;

import java.util.List;

public interface UserConverter {
	List<User> convert(List<String> lines);

	ConvertorType getType();
}
