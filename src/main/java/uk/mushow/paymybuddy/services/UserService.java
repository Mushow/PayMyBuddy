package uk.mushow.paymybuddy.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.dtos.RegisterDTO;
import uk.mushow.paymybuddy.exceptions.AlreadyFriendsException;
import uk.mushow.paymybuddy.exceptions.EmailAlreadyInUseException;
import uk.mushow.paymybuddy.exceptions.UserNotFoundException;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Log4j2
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private WalletService walletService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createWallet(User newUser) {
        Wallet wallet = new Wallet();
        wallet.setUser(newUser);
        wallet.setBalance(BigDecimal.valueOf(0.0));
        walletService.save(wallet);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            String userNotFoundMessage = "The user with email " + email + " was not found.";
            log.error(userNotFoundMessage);
            return new UserNotFoundException(userNotFoundMessage);
        });
    }

    @Override
    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addFriendByEmail(String currentUserEmail, String friendEmail) {
        if (currentUserEmail.equals(friendEmail)) {
            throw new IllegalArgumentException("Cannot add yourself as a friend.");
        }

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Current user not found."));
        User friendUser = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new UserNotFoundException("Friend user not found."));

        if (areAlreadyFriends(currentUser, friendUser)) {
            throw new AlreadyFriendsException("You are already friends with " + friendUser.getUsername() + ".");
        }

        addEachOtherAsFriends(currentUser, friendUser);

        userRepository.save(currentUser);
        userRepository.save(friendUser);
    }

    @Override
    public boolean areAlreadyFriends(User user1, User user2) {
        return user1.getFriendsList().contains(user2) || user2.getFriendsList().contains(user1);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addEachOtherAsFriends(User user1, User user2) {
        user1.getFriendsList().add(user2);
        user2.getFriendsList().add(user1);
    }

    @Override
    public Set<User> getFriends(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."))
                .getFriendsList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteFriendById(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Friend not found."));

        user.getFriendsList().remove(friend);
        friend.getFriendsList().remove(user);

        userRepository.save(user);
        userRepository.save(friend);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}
