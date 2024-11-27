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

	//todo переписати логіку з використанням пошуку РНОКПП в двопоточному варіанті який буде шукати сторінками (чотні та не чотні)
//	List<String> allData = ... // 1000 записей с уникальными данными
//	int mid = allData.size() / 2;
//
//	List<String> firstHalf = allData.subList(0, mid);
//	List<String> secondHalf = allData.subList(mid, allData.size());

//	ExecutorService executor = Executors.newFixedThreadPool(2);
//
//	Callable<List<String>> task1 = () -> checkDatabase(firstHalf, false, 100); // Нечетные страницы
//	Callable<List<String>> task2 = () -> checkDatabase(secondHalf, true, 100); // Четные страницы
//
//	Future<List<String>> future1 = executor.submit(task1);
//	Future<List<String>> future2 = executor.submit(task2);
//
//	// Ожидание завершения потоков
//	List<String> result1 = future1.get();
//	List<String> result2 = future2.get();
//
//	// Слияние результатов
//	List<String> finalResults = new ArrayList<>(result1);
//finalResults.addAll(result2);
//
//// Завершение пула потоков
//executor.shutdown();

//	public List<String> checkDatabase(List<String> data, boolean checkEvenPages, int pageSize) {
//		List<String> foundData = new ArrayList<>();
//		int page = checkEvenPages ? 0 : 1; // Стартовая страница (четная или нечетная)
//		Page<String> result;
//
//		do {
//			Pageable pageable = PageRequest.of(page, pageSize);
//			result = userRepository.findExistingUserNamesWithPagination(data, pageable);
//			foundData.addAll(result.getContent());
//			page += 2; // Переход на следующую четную/нечетную страницу
//		} while (!result.isEmpty());
//
//		return foundData;
//	}
}


