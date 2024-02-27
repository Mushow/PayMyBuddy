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
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.UserRepository;

import java.math.BigDecimal;

@Service
@Log4j2
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private WalletService walletService;

    public void createUser(RegisterDTO registerDTO) {
        if(userRepository.existsByUsername(registerDTO.username())) {
            String invalidUsernameMessage = "The username is already in use.";
            log.error(invalidUsernameMessage);
            throw new EmailAlreadyInUseException(invalidUsernameMessage);
        }

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

        newUser = userRepository.save(newUser);
        createWallet(newUser);
    }

    private void createWallet(User newUser) {
        Wallet wallet = new Wallet();
        wallet.setUser(newUser);
        wallet.setBalance(BigDecimal.valueOf(0.0));
        walletService.save(wallet);
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
