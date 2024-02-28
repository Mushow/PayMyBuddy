package uk.mushow.paymybuddy.services;

import uk.mushow.paymybuddy.dtos.RegisterDTO;
import uk.mushow.paymybuddy.models.User;

import java.util.Set;

public interface IUserService {

    void createUser(RegisterDTO registerDTO);
    void createWallet(User newUser);
    void deleteUser(User user);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    void addEachOtherAsFriends(User user, User friend);
    Set<User> getFriends(Long userId);
    void deleteFriendById(Long userId, Long friendId);
    boolean areAlreadyFriends(User user, User friend);
    boolean doesEmailExist(String email);
    void addFriendByEmail(String currentUserEmail, String friendEmail);

}
