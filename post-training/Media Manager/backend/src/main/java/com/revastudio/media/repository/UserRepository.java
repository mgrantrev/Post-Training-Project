package com.revastudio.media.repository;

import com.revastudio.media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    java.util.List<User> findBySupportRepId(Integer supportRepId);
}
