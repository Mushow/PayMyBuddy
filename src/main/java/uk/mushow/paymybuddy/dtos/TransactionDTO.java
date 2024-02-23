package uk.mushow.paymybuddy.dtos;

import java.math.BigDecimal;


public record TransactionDTO(String senderAccountNumber, String receiverAccountNumber, String description,
                             BigDecimal amount, BigDecimal transactionFee, String timestamp) {}
