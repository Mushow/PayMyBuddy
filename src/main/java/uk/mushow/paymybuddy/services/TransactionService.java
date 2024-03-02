package uk.mushow.paymybuddy.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.dtos.TransactionDTO;
import uk.mushow.paymybuddy.models.Transaction;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.TransactionRepository;
import uk.mushow.paymybuddy.repositories.WalletRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletRepository walletRepository, WalletService walletService, UserService userService, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionDTO> getTransactionsDTO(Long userId) {
        List<Transaction> transactions = walletRepository.findByIssuerWallet_User_Id(userId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void transfer(Long userId, String friendEmail, BigDecimal amount, String description) {
        Wallet issuerWallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Issuer wallet not found"));
        Wallet receiverWallet = walletRepository.findByUserId(userService.getUserByEmail(friendEmail).getId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver wallet not found"));

        if (issuerWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        walletService.withdrawFromBalance(userId, amount);
        walletService.topUpBalance(userService.getUserByEmail(friendEmail).getId(), amount);

        Transaction issuerTransaction = createTransaction(issuerWallet, receiverWallet, amount, description);
        transactionRepository.save(issuerTransaction);

        Transaction receiverTransaction = createTransaction(receiverWallet, issuerWallet, amount, description);
        transactionRepository.save(receiverTransaction);
    }

    @Override
    public Transaction createTransaction(Wallet issuerWallet, Wallet receiverWallet, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setIssuerWallet(issuerWallet);
        transaction.setReceiverWallet(receiverWallet);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTimestamp(new Date());
        return transaction;
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getIssuerWallet().getUser().getUsername(),
                transaction.getReceiverWallet().getUser().getUsername(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getTimestamp().toString());
    }

}