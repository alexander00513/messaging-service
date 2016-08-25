package com.gmail.bogatyr.alexander.intech.repository;

import com.gmail.bogatyr.alexander.intech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Alexander Bogatyrenko on 12.08.16.
 *
 * This class represents...
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByLogin(String login);
}
