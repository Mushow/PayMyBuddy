package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/friends")
    public String addFriend(@RequestParam("email") String email, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (userService.doesEmailExist(email)) {
            userService.addFriendByEmail(customUserDetails.getEmail(), email);
            model.addAttribute("message", "Friend added successfully!");
        } else {
            model.addAttribute("error", "Email does not exist in the database.");
        }
        return "redirect:/friends"; // Redirect to avoid duplicate submissions
    }

}
