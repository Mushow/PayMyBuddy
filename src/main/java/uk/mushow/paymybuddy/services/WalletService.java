package uk.mushow.paymybuddy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.WalletRepository;

import java.math.BigDecimal;

@Service
public class WalletService implements IWalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public BigDecimal getBalance(Long userId) {
        return walletRepository.findByUserId(userId)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

}

