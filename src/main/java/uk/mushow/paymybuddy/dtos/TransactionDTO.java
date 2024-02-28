package uk.mushow.paymybuddy.dtos;

import java.math.BigDecimal;


public record TransactionDTO(String issuerName, String receiverName, String description,
                             BigDecimal amount, String timestamp) {}
