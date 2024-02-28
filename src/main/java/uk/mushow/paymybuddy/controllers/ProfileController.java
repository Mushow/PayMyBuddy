package uk.mushow.paymybuddy.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("user", customUserDetails);
        return "profile";
    }

}
