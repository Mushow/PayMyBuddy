package uk.mushow.paymybuddy.dtos;

import java.math.BigDecimal;

public record WalletDTO(Long userId, BigDecimal balance) {}
