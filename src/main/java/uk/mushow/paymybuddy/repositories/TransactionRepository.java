package uk.mushow.paymybuddy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.mushow.paymybuddy.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
