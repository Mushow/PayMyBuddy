package uk.mushow.paymybuddy.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.dtos.RegisterDTO;
import uk.mushow.paymybuddy.exceptions.EmailAlreadyInUseException;
import uk.mushow.paymybuddy.exceptions.UserNotFoundException;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.repositories.UserRepository;

@Service
@Log4j2
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder argon2PasswordEncoder;

    public void createUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.email())) {
            String invalidEmailMessage = "The email is already in use.";
            log.error(invalidEmailMessage);
            throw new EmailAlreadyInUseException(invalidEmailMessage);
        }

        User newUser = new User();
        newUser.setUsername(registerDTO.username());
        newUser.setEmail(registerDTO.email());
        String hashedPassword = argon2PasswordEncoder.encode(registerDTO.password());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            String userNotFoundMessage = "The user with email " + email + " was not found.";
            log.error(userNotFoundMessage);
            return new UserNotFoundException(userNotFoundMessage);
        });
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            String userNotFoundMessage = "The user with username " + username + " was not found.";
            log.error(userNotFoundMessage);
            return new UserNotFoundException(userNotFoundMessage);
        });
    }

}
