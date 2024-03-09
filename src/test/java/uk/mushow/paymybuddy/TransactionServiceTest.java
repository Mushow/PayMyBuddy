package uk.mushow.paymybuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.mushow.paymybuddy.dtos.TransactionDTO;
import uk.mushow.paymybuddy.models.Transaction;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.TransactionRepository;
import uk.mushow.paymybuddy.repositories.WalletRepository;
import uk.mushow.paymybuddy.services.TransactionService;
import uk.mushow.paymybuddy.services.UserService;
import uk.mushow.paymybuddy.services.WalletService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private UserService userService;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Wallet issuerWallet, receiverWallet;
    private Transaction transaction;

    private User issuerUser, receiverUser;

    @BeforeEach
    void setUp() {
        issuerWallet = new Wallet();
        issuerWallet.setId(1L);
        issuerWallet.setBalance(new BigDecimal("100.00"));

        receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(new BigDecimal("50.00"));

        issuerUser = new User();
        issuerUser.setUsername("issuerUsername");
        issuerUser.setEmail("user@example.com");
        issuerWallet.setUser(issuerUser);

        receiverUser = new User();
        receiverUser.setUsername("receiverUsername");
        receiverUser.setEmail("friend@example.com");
        receiverWallet.setUser(receiverUser);


        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setIssuerWallet(issuerWallet);
        transaction.setReceiverWallet(receiverWallet);
        transaction.setAmount(new BigDecimal("20.00"));
        transaction.setDescription("Test transfer");
        transaction.setTimestamp(new Date());
    }

    @Test
    void testGetIssuerTransactionsDTO() {
        when(walletRepository.findIssuerWalletTransactions(any(Long.class))).thenReturn(Collections.singletonList(transaction));
        List<TransactionDTO> transactionDTOS = transactionService.getIssuerTransactionsDTO(1L);

        assertEquals(1, transactionDTOS.size());
        TransactionDTO dto = transactionDTOS.get(0);
        assertEquals("Test transfer", dto.description());

        verify(walletRepository).findIssuerWalletTransactions(any(Long.class));
    }

    @Test
    void testGetReceiverTransactionsDTO() {
        when(walletRepository.findReceiverWalletTransactions(any(Long.class))).thenReturn(Collections.singletonList(transaction));
        List<TransactionDTO> transactionDTOS = transactionService.getReceiverTransactionsDTO(2L);

        assertEquals(1, transactionDTOS.size());
        TransactionDTO dto = transactionDTOS.get(0);
        assertEquals("Test transfer", dto.description());

        verify(walletRepository).findReceiverWalletTransactions(any(Long.class));
    }

    @Test
    void createTransactionTest() {
        Transaction transaction = transactionService.createTransaction(issuerWallet, receiverWallet, new BigDecimal("20.00"), "Test transfer");
        assertEquals(issuerWallet, transaction.getIssuerWallet());
        assertEquals(receiverWallet, transaction.getReceiverWallet());
        assertEquals(new BigDecimal("20.00"), transaction.getAmount());
        assertEquals("Test transfer", transaction.getDescription());
    }

    @Test
    void testTransfer() {
        when(walletRepository.findByUserId(issuerUser.getId())).thenReturn(Optional.of(issuerWallet));
        when(userService.getUserByEmail(receiverUser.getEmail())).thenReturn(receiverUser);
        when(walletRepository.findByUserId(receiverUser.getId())).thenReturn(Optional.of(receiverWallet));

        transactionService.transfer(issuerUser.getId(), receiverUser.getEmail(), new BigDecimal("20.00"), "Test transfer");

        verify(walletService).withdrawFromBalance(issuerUser.getId(), new BigDecimal("20.00"));
        verify(walletService).topUpBalance(receiverUser.getId(), new BigDecimal("20.00"));
        verify(transactionRepository).save(any(Transaction.class));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(issuerUser.getId(), receiverUser.getEmail(), new BigDecimal("5000.00"), "Test transfer");
        });

        assertEquals("Insufficient funds", thrown.getMessage());
    }

}
