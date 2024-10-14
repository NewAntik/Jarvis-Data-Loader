package ua.jarvis.data.loader.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.repository.UserRepository;
import ua.jarvis.data.loader.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;

	private Set<String> rnokpps;

	public UserServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public void saveUsers(final List<User> users) {
		LOG.info("saveUsers was called with users: {}", users);
		final List<User> sortedUsers = new ArrayList<>();

		rnokpps = users.stream().map(User::getRnokpp).collect(Collectors.toSet());
		sortedUsers.addAll(users.stream()
			.filter(u -> rnokpps.contains(u.getRnokpp()))
			.toList());

		userRepository.saveAllAndFlush(sortedUsers);

		LOG.info("{} users was saved!", rnokpps.size());
	}
}
