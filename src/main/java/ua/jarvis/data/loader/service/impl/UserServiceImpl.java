package ua.jarvis.data.loader.service.impl;

import jakarta.transaction.Transactional;
import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.jarvis.data.loader.core.model.User;
import ua.jarvis.data.loader.repository.UserRepository;
import ua.jarvis.data.loader.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		final List<User> sortedUsers = users.stream()
			.filter(user -> rnokpps.add(user.getRnokpp()))
			.toList();
		try {
			userRepository.saveAllAndFlush(sortedUsers);
			LOG.info("Successfully saved {} users to the database.", sortedUsers.size());
		} catch (final RuntimeException e) {
			LOG.warn("Constraint violation occurred while saving users. Cause: {}", e.getCause());
			System.out.println("Якась хрінь.");
		}

		LOG.info("{} users was saved!", rnokpps.size());

		return (long) (users.size() - sortedUsers.size());
	}
}
