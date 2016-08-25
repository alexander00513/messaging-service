package com.gmail.bogatyr.alexander.intech.service;

import com.gmail.bogatyr.alexander.intech.domain.Authority;
import com.gmail.bogatyr.alexander.intech.domain.Message;
import com.gmail.bogatyr.alexander.intech.domain.User;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO;
import com.gmail.bogatyr.alexander.intech.repository.AuthorityRepository;
import com.gmail.bogatyr.alexander.intech.repository.MessageRepository;
import com.gmail.bogatyr.alexander.intech.repository.UserRepository;
import com.gmail.bogatyr.alexander.intech.util.SecurityUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.gmail.bogatyr.alexander.intech.constant.Authorities.USER;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Alexander Bogatyrenko on 12.08.16.
 * <p>
 * This class represents...
 */
@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        logger.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getPassword(),
                    grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                "database"));
    }

    @Transactional
    public boolean registration(UserDTO userDTO) {
        logger.debug("Starting registration user - {}", userDTO);
        String login = userDTO.getLogin();
        if (!isEmpty(login)) {
            Optional<User> userFromDatabase = userRepository.findOneByLogin(login.toLowerCase(Locale.ENGLISH));
            if (userFromDatabase.isPresent()) {
                return false;
            }
        }
        return !isNull(create(userDTO));
    }

    @Transactional
    public User changePass(UserDTO userDTO) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        logger.debug("Starting change pass for user - {}", currentUserLogin);
        Optional<User> userFromDatabase = userRepository.findOneByLogin(currentUserLogin);
        if (userFromDatabase.isPresent()) {
            User user = userFromDatabase.get();
            String password = userDTO.getPassword();
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            if (matches) {
                user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
                return userRepository.save(user);
            } else {
                logger.error("Old password is incorrect");
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> {
            UserDTO userDTO = mapper.map(u, UserDTO.class);
            Set<Authority> authorities = u.getAuthorities();
            if (!CollectionUtils.isEmpty(authorities)) {
                userDTO.setAuthorities(authorities.stream().map(Authority::getName).collect(Collectors.toSet()));
            }
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public User create(UserDTO userDTO) {
        logger.debug("Starting create a new user - {}", userDTO);
        User user = mapper.map(userDTO, User.class);

        String password = userDTO.getPassword();
        if (isEmpty(password)) {
            password = RandomStringUtils.randomAlphanumeric(20);
        }
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        Set<Authority> authorities = new HashSet<>();
        Set<String> sourceAuthorities = userDTO.getAuthorities();
        if (!CollectionUtils.isEmpty(sourceAuthorities)) {
            sourceAuthorities.forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
        } else {
            authorities.add(authorityRepository.findOne(USER));
        }
        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDTO read(Long id) {
        User user = userRepository.findOne(id);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        Set<Authority> authorities = user.getAuthorities();
        if (!CollectionUtils.isEmpty(authorities)) {
            userDTO.setAuthorities(authorities.stream().map(Authority::getName).collect(Collectors.toSet()));
        }
        return userDTO;
    }

    @Transactional
    public User update(UserDTO userDTO) {
        logger.debug("Starting update a user - {}", userDTO);
        Long id = userDTO.getId();
        User user = userRepository.findOne(id);
        if (!isNull(user)) {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setLogin(userDTO.getLogin());

            String password = userDTO.getPassword();
            String newPassword = userDTO.getNewPassword();
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            if (matches && !isEmpty(newPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }

            Set<Authority> authorities = user.getAuthorities();
            authorities.clear();
            Set<String> sourceAuthorities = userDTO.getAuthorities();
            if (!CollectionUtils.isEmpty(sourceAuthorities)) {
                sourceAuthorities.forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
            }
            return userRepository.save(user);
        } else {
            logger.error("User with id - {} - not found in db", id);
        }
        return null;
    }

    @Transactional
    public boolean delete(Long id) {
        logger.debug("Starting delete user with id - {}", id);
        List<Message> users = messageRepository.findByFromIdOrToId(id, id);
        if (CollectionUtils.isEmpty(users)) {
            userRepository.delete(id);
            return true;
        }
        return false;
    }
}
