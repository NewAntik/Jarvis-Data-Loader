package ua.jarvis.data.loader.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.repository.UserRepository;
import ua.jarvis.data.loader.service.UserService;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;

	public UserServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public void saveUsers(final Set<User> users) {
		LOG.info("saveUsers was called with users: {}", users.toString());
		userRepository.saveAllAndFlush(users);
	}
}
