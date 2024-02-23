package uk.mushow.paymybuddy.dtos;

import java.math.BigDecimal;

public record WalletDTO(String accountNumber, BigDecimal balance) {}
