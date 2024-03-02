package uk.mushow.paymybuddy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "issuerWallet")
    private List<Transaction> issuedTransactions;

    @OneToMany(mappedBy = "receiverWallet")
    private List<Transaction> receivedTransactions;

}