package uk.mushow.paymybuddy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 22, nullable = false)
    private String accountNumber;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private User user;

}