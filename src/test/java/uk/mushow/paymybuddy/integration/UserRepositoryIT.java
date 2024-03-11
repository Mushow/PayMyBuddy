package uk.mushow.paymybuddy.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.mushow.paymybuddy.models.User;
import uk.mushow.paymybuddy.repositories.UserRepository;

import javax.sql.DataSource;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("data.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    @Test
    @Transactional
    public void findByEmailTest(){
        User user = userRepository.findByEmail("user1@example.com").get();
        Assertions.assertThat(user.getUsername()).isEqualTo("User1");
        Assertions.assertThat(user.getPassword()).isEqualTo("password1"); // Assuming plain text for simplicity
    }

    @Test
    @Transactional
    public void existsByEmailTest(){
        boolean userExists = userRepository.existsByEmail("user1@example.com");
        Assertions.assertThat(userExists).isTrue();
    }

    @Test
    @Transactional
    public void existsByUsernameTest(){
        boolean userExists = userRepository.existsByUsername("User1");
        Assertions.assertThat(userExists).isTrue();
    }

}
