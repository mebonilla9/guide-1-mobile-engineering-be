package co.edu.umb.guide1mobileengineering.domain.repository;

import co.edu.umb.guide1mobileengineering.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findUserByEmail(String email);
}