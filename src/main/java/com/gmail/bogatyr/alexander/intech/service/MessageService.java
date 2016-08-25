package com.gmail.bogatyr.alexander.intech.service;

import com.gmail.bogatyr.alexander.intech.domain.Message;
import com.gmail.bogatyr.alexander.intech.domain.User;
import com.gmail.bogatyr.alexander.intech.dto.MessageDTO;
import com.gmail.bogatyr.alexander.intech.repository.MessageRepository;
import com.gmail.bogatyr.alexander.intech.repository.UserRepository;
import com.gmail.bogatyr.alexander.intech.util.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by Alexander Bogatyrenko on 12.08.16.
 * <p>
 * This class represents...
 */
@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<MessageDTO> findAll() {
        return messageRepository.findAll().stream().map(m -> mapper.map(m, MessageDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public List<Message> create(MessageDTO messageDTO) {
        List<Long> recipients = messageDTO.getRecipients();
        int size = recipients.size();
        List<Message> messages = new ArrayList<>(size);
        logger.debug("Starting create message for {} recipients", size);
        if (!isEmpty(recipients)) {
            for (Long recipient : recipients) {
                logger.debug("Create message for recipient - {}", recipient);
                Message message = mapper.map(messageDTO, Message.class);
                String currentUserLogin = SecurityUtils.getCurrentUserLogin();
                Optional<User> currentUser = userRepository.findOneByLogin(currentUserLogin);
                if (currentUser.isPresent()) {
                    logger.debug("Message sender is - {}", currentUserLogin);
                    message.setFrom(currentUser.get());
                } else {
                    logger.error("Current user is null");
                }
                User to = userRepository.findOne(recipient);
                if (!isNull(to)) {
                    message.setTo(to);
                } else {
                    logger.error("Recipient with id - {} not found", recipient);
                }
                messages.add(message);
            }
        }
        return messageRepository.save(messages);
    }

    @Transactional(readOnly = true)
    public MessageDTO read(Long id) {
        logger.debug("Starting read message with id - {}", id);
        return mapper.map(messageRepository.findOne(id), MessageDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("Starting delete message with id - {}", id);
        messageRepository.delete(id);
    }
}
