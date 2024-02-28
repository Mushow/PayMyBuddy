package uk.mushow.paymybuddy.services;

import uk.mushow.paymybuddy.models.Wallet;

import java.math.BigDecimal;

public interface IWalletService {

    BigDecimal getBalance(Long userId);
    void save(Wallet wallet);

}
