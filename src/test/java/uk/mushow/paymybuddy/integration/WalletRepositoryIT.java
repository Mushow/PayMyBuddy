package uk.mushow.paymybuddy.integration;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.models.Transaction;
import uk.mushow.paymybuddy.models.Wallet;
import uk.mushow.paymybuddy.repositories.WalletRepository;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class WalletRepositoryIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("data.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);
    }


    @Test
    public void findByUserId_test(){
        Wallet wallet = walletRepository.findByUserId(1L).get();
        Assertions.assertThat(wallet.getUser().getUsername()).isEqualTo("User1");
    }

    @Test
    public void findIssuerWalletTransactions_test(){
        List<Transaction> transactions = walletRepository.findIssuerWalletTransactions(1L);
        Assertions.assertThat(transactions.size()).isEqualTo(1);
    }

    @Test
    public void findReceiverWalletTransactions_test(){
        List<Transaction> transactions = walletRepository.findReceiverWalletTransactions(1L);
        Assertions.assertThat(transactions.size()).isEqualTo(1);
    }
}
