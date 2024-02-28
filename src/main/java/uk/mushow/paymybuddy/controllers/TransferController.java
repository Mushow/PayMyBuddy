package uk.mushow.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransferController {

    @GetMapping("/transfer")
    public String showTransfer() {
        return "transfer";
    }

}
