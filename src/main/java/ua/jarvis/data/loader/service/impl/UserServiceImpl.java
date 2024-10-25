package ua.jarvis.data.loader.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.repository.UserRepository;
import ua.jarvis.data.loader.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;

	private Set<String> rnokpps = new HashSet<>();

	public UserServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public Long saveUsers(final List<User> users) {
		Long duplicates = 0L;
		final Set<String> allRnokpps = users.stream().map(User::getRnokpp).collect(Collectors.toSet());

		for (final String rnokpp : allRnokpps) {
			if (rnokpps.contains(rnokpp)) {
				duplicates++;
			} else {
				rnokpps.add(rnokpp);
			}
		}
		final List<User> sortedUsers = users.stream()
			.filter(u -> rnokpps.contains(u.getRnokpp()))
			.toList();

		duplicates = (long) (users.size() - sortedUsers.size());

		userRepository.saveAllAndFlush(sortedUsers);

		LOG.info("{} users was saved!", rnokpps.size());

		return duplicates;
	}
}
