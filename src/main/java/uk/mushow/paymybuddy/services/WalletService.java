package uk.mushow.paymybuddy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.WalletRepository;

import java.math.BigDecimal;

@Transactional
@Service
public class WalletService implements IWalletService {

    @Autowired
    private WalletRepository walletRepository;

    public static final BigDecimal FEES = new BigDecimal("0.005");

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

    @Override
    public void topUpBalance(Long userId, BigDecimal amount) {
        BigDecimal amountToTopUp = amount.subtract(amount.multiply(FEES));
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(amountToTopUp));
        save(wallet);
    }

    @Override
    public void withdrawFromBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .filter(w -> w.getBalance().compareTo(amount) >= 0)
                .orElseThrow(() -> new IllegalArgumentException("Insufficient balance"));
        wallet.setBalance(wallet.getBalance().subtract(amount));
        save(wallet);
    }
}

