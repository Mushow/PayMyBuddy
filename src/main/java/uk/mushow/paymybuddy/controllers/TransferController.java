package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.services.WalletService;
import uk.mushow.paymybuddy.userdetails.CustomUserDetails;

import java.math.BigDecimal;
import java.util.Set;

@Controller
public class TransferController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @GetMapping("/transfer")
    public String showTransfer(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        BigDecimal availableFunds = walletService.getBalance(customUserDetails.getUserId());
        Set<User> friends = userService.getFriends(customUserDetails.getUserId());
        model.addAttribute("friends", friends);
        model.addAttribute("availableFunds", availableFunds);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        redirectAttributes.addFlashAttribute("success", "Transfer completed!.");
        return "redirect:/transfer";
    }

}
