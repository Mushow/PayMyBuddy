package uk.mushow.paymybuddy.dtos;

import java.math.BigDecimal;


public record TransactionDTO(Long issuerWalletId, Long receiverWalletId, String description,
                             BigDecimal amount, String timestamp) {}
