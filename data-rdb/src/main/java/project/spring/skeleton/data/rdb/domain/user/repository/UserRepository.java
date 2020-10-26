package project.spring.skeleton.data.rdb.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.spring.skeleton.data.rdb.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> { }