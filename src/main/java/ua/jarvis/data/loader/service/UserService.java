package ua.jarvis.data.loader.service;

import ua.jarvis.data.loader.core.model.User;

import java.util.Set;

public interface UserService {
	void saveUsers(Set<User> users);
}
