package ua.jarvis.data.loader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.jarvis.data.loader.core.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
