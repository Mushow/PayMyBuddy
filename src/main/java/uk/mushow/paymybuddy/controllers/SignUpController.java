package uk.mushow.paymybuddy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.mushow.paymybuddy.dtos.RegisterDTO;
import uk.mushow.paymybuddy.exceptions.EmailAlreadyInUseException;
import uk.mushow.paymybuddy.services.UserService;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO("", "", ""));
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("registerDTO") RegisterDTO registerDTO, Model model) {
        try {
            userService.createUser(registerDTO);
            return "redirect:/login";
        } catch (EmailAlreadyInUseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }
}
