package com.gmail.bogatyr.alexander.intech.repository;

import com.gmail.bogatyr.alexander.intech.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Alexander Bogatyrenko on 12.08.16.
 * <p>
 * This class represents...
 */

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByFromIdOrToId(Long from, Long to);
}
