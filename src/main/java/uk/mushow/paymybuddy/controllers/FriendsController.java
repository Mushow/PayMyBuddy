package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

@Controller
public class FriendsController {

    @Autowired
    private UserService userService;

    @GetMapping("/friends")
    public String getFriendsPage() {
        return "friends";
    }

    @PostMapping("/friends/add")
    public String addFriend(@RequestParam("email") String email, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            if (userService.doesEmailExist(email)) {
                userService.addFriendByEmail(customUserDetails.getEmail(), email);
                redirectAttributes.addFlashAttribute("message", "Friend added successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Email does not exist in the database.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/friends";
    }

    @PostMapping("/friends/delete")
    public String deleteFriend(@RequestParam("deleteById") Long friendId, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deleteFriendById(customUserDetails.getUserId(), friendId);
        redirectAttributes.addFlashAttribute("message", "Friend deleted successfully!");
        return "redirect:/home";
    }

}
