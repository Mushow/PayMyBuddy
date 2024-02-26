package uk.mushow.paymybuddy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.WalletRepository;

import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public BigDecimal getBalance(Long userId) {
        System.out.println("Wallet: " + walletRepository.findByUserId(userId));
        return walletRepository.findByUserId(userId)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
    }

}

