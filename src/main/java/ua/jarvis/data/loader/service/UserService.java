package ua.jarvis.data.loader.service;

import ua.jarvis.data.loader.core.model.User;

import java.util.List;

public interface UserService {
	Long saveUsers(List<User> users);
}
