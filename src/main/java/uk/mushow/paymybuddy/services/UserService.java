package uk.mushow.paymybuddy.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.exceptions.EmailAlreadyInUseException;
import uk.mushow.paymybuddy.exceptions.UserNotFoundException;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.repositories.UserRepository;
import uk.mushow.paymybuddy.services.utils.ArgonUtil;

@Service
@Log4j2
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            String invalidEmailMessage = "The email is already in use.";
            log.error(invalidEmailMessage);
            throw new EmailAlreadyInUseException(invalidEmailMessage);
        }
        String hashedPassword = ArgonUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
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



}
