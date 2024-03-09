package uk.mushow.paymybuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.WalletRepository;
import uk.mushow.paymybuddy.services.WalletService;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void topUpBalanceSuccess() {
        Long userId = 1L;
        BigDecimal initialBalance = new BigDecimal("100");
        BigDecimal topUpAmount = new BigDecimal("50");
        BigDecimal expectedFee = WalletService.FEES.multiply(topUpAmount);
        BigDecimal expectedTopUpAmount = topUpAmount.subtract(expectedFee);
        BigDecimal expectedFinalBalance = initialBalance.add(expectedTopUpAmount);

        Wallet mockWallet = new Wallet();
        mockWallet.setBalance(initialBalance);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(mockWallet));

        walletService.topUpBalance(userId, topUpAmount);

        assertEquals(expectedFinalBalance, mockWallet.getBalance());
    }

    @Test
    void withdrawFromBalanceSuccess() {
        Long userId = 1L;
        BigDecimal initialBalance = new BigDecimal("100");
        BigDecimal withdrawAmount = new BigDecimal("50");
        BigDecimal expectedFinalBalance = initialBalance.subtract(withdrawAmount);

        Wallet mockWallet = new Wallet();
        mockWallet.setBalance(initialBalance);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(mockWallet));

        walletService.withdrawFromBalance(userId, withdrawAmount);

        assertEquals(expectedFinalBalance, mockWallet.getBalance());
    }

    @Test
    void getBalanceSuccess() {
        Long userId = 1L;
        BigDecimal initialBalance = new BigDecimal("100");

        Wallet mockWallet = new Wallet();
        mockWallet.setBalance(initialBalance);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(mockWallet));

        BigDecimal balance = walletService.getBalance(userId);

        assertEquals(initialBalance, balance);
    }



}

