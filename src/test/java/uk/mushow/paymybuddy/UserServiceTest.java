package uk.mushow.paymybuddy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import uk.mushow.paymybuddy.dtos.RegisterDTO;
import uk.mushow.paymybuddy.exceptions.AlreadyFriendsException;
import uk.mushow.paymybuddy.exceptions.EmailAlreadyInUseException;
import uk.mushow.paymybuddy.exceptions.UserNotFoundException;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.repositories.UserRepository;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.services.WalletService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private WalletService walletService;

    private User currentUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setEmail("current@example.com");
        currentUser.setUsername("currentUser");
        currentUser.setFriendsList(new HashSet<>());
        currentUser.setId(1L);

        friendUser = new User();
        friendUser.setEmail("friend@example.com");
        friendUser.setUsername("friendUser");
        friendUser.setFriendsList(new HashSet<>());
        friendUser.setId(2L);
    }

    @Test
    void getUserByIdFound() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User resultUser = userService.getUserById(userId);

        assertEquals(expectedUser, resultUser);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserByIdNotFound() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found.", exception.getMessage());
    }


    @Test
    void getUserByEmailFound() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User resultUser = userService.getUserByEmail(email);

        assertEquals(expectedUser, resultUser);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserByEmailNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));

        assertEquals("The user with email " + email + " was not found.", exception.getMessage());
    }

    @Test
    void createUserUsernameAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("testUsername", "test@example.com", "password");

        when(userRepository.existsByUsername(registerDTO.username())).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(registerDTO));

        assertEquals("The username is already in use.", exception.getMessage());
        verify(userRepository).existsByUsername(registerDTO.username());
        verify(userRepository, never()).existsByEmail(registerDTO.email());
    }

    @Test
    void createUserEmailAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("testUsername", "test@example.com", "password");

        when(userRepository.existsByUsername(registerDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.email())).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(registerDTO));

        assertEquals("The email is already in use.", exception.getMessage());
        verify(userRepository).existsByUsername(registerDTO.username());
        verify(userRepository).existsByEmail(registerDTO.email());
    }

    @Test
    void createUserSuccess() {
        RegisterDTO registerDTO = new RegisterDTO("testUsername", "test@example.com", "password");
        User savedUser = new User();

        when(userRepository.existsByUsername(registerDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.email())).thenReturn(false);
        when(argon2PasswordEncoder.encode(registerDTO.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.createUser(registerDTO);

        verify(userRepository).existsByUsername(registerDTO.username());
        verify(userRepository).existsByEmail(registerDTO.email());
        verify(argon2PasswordEncoder).encode(registerDTO.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void doesEmailExistTest() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean result = userService.doesEmailExist(email);
        assertTrue(result);
    }

    @Test
    public void testAreAlreadyFriends() {
        User user1 = new User();
        User user2 = new User();

        user1.setFriendsList(new HashSet<>(Set.of(user2)));
        assertTrue(userService.areAlreadyFriends(user1, user2));
    }

    @Test
    void addSelfAsFriendThrowsException() {
        String email = "current@example.com";

        assertThrows(IllegalArgumentException.class, () -> userService.addFriendByEmail(email, email));
    }

    @Test
    void currentUserNotFoundThrowsException() {
        String currentUserEmail = "current@example.com";
        String friendEmail = "friend@example.com";

        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addFriendByEmail(currentUserEmail, friendEmail));
    }

    @Test
    void friendUserNotFoundThrowsException() {
        String currentUserEmail = "current@example.com";
        String friendEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail(friendEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addFriendByEmail(currentUserEmail, friendEmail));
    }

    @Test
    void alreadyFriendsThrowsException() {
        currentUser.getFriendsList().add(friendUser);
        friendUser.getFriendsList().add(currentUser);

        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail(friendUser.getEmail())).thenReturn(Optional.of(friendUser));

        assertThrows(AlreadyFriendsException.class, () -> userService.addFriendByEmail(currentUser.getEmail(), friendUser.getEmail()));
    }

    @Test
    void addFriendSuccessfully() {
        currentUser.setFriendsList(new HashSet<>());
        friendUser.setFriendsList(new HashSet<>());

        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail(friendUser.getEmail())).thenReturn(Optional.of(friendUser));

        userService.addFriendByEmail(currentUser.getEmail(), friendUser.getEmail());

        assertTrue(currentUser.getFriendsList().contains(friendUser));
        assertTrue(friendUser.getFriendsList().contains(currentUser));

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void getFriendsSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Set<User> friends = new HashSet<>();
        friends.add(new User()); // Add a friend to the set
        user.setFriendsList(friends);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Set<User> result = userService.getFriends(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(friends.size(), result.size());
        verify(userRepository).findById(userId);
    }

    @Test
    void getFriendsUserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getFriends(userId));
    }

    @Test
    void deleteFriendByIdSuccess() {
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User();
        User friend = new User();
        user.setId(userId);
        friend.setId(friendId);
        user.setFriendsList(new HashSet<>(Collections.singletonList(friend)));
        friend.setFriendsList(new HashSet<>(Collections.singletonList(user)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));

        userService.deleteFriendById(userId, friendId);

        assertFalse(user.getFriendsList().contains(friend));
        assertFalse(friend.getFriendsList().contains(user));
        verify(userRepository).save(user);
        verify(userRepository).save(friend);
    }

    @Test
    void deleteFriendByIdUserNotFound() {
        Long userId = 99L;
        Long friendId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteFriendById(userId, friendId));
    }

    @Test
    void deleteFriendByIdFriendNotFound() {
        Long userId = 1L;
        Long friendId = 99L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteFriendById(userId, friendId));
    }


}
