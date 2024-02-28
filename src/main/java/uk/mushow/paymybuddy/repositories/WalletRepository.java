package uk.mushow.paymybuddy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.models.Transaction;
import uk.mushow.paymybuddy.models.Wallet;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.issuerWallet.user.id = :userId")
    List<Transaction> findByIssuerWallet_User_Id(Long userId);
}
