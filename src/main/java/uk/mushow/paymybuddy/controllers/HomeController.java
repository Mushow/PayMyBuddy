package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

@Controller
public class HomeController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/home")
    public String getHomePage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        model.addAttribute("balance", walletService.getBalance(userId));
        return "home";
    }

}
