package uk.mushow.paymybuddy.services;

import uk.mushow.paymybuddy.dtos.TransactionDTO;
import uk.mushow.paymybuddy.models.Transaction;
import uk.mushow.paymybuddy.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {

    List<TransactionDTO> getTransactionsDTO(Long userId);

    void transfer(Long userId, String friendEmail, BigDecimal amount, String description);

    Transaction createTransaction(Wallet issuerWallet, Wallet receiverWallet, BigDecimal amount, String description);
}
