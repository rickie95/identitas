package it.riccardomalavolti.identitas.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import it.riccardomalavolti.identitas.model.UserCredentials;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserCredentialRepositoryTest {

    @Autowired
    private UserCredentialRepository repository;

    @Test
    public void testJPAMapping(){
        UserCredentials credentials = new UserCredentials("username", "password");
        UserCredentials saved = repository.save(credentials);
        assertThat(saved).isEqualTo(credentials);
    }

    @Test
    public void testExistShouldReturnTrueIfEntityIsPresentInDB(){
        UserCredentials credentials = new UserCredentials("username", "password");
        repository.save(credentials);

        assertThat(repository.exists(Example.of(credentials))).isTrue();
    }

}