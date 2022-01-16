package it.riccardomalavolti.identitas;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.repositories.UserCredentialRepository;
import it.riccardomalavolti.identitas.security.TokenIssuer;
import it.riccardomalavolti.identitas.services.AuthenticationService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({AuthenticationService.class, TokenIssuer.class})
public class AuthenticationServiceRepositoryIT {

    @Autowired
    TokenIssuer tokenService;

    @Autowired
    UserCredentialRepository credentialRepository;

    AuthenticationService authenticationService;

    @Before
    public void setup(){
        authenticationService = new AuthenticationService(credentialRepository, tokenService);
        credentialRepository.deleteAll();
        credentialRepository.flush();
    }

    @Test
    public void testServiceChecksUserCredentialsThroughRepository(){
        UserCredentials credentials = new UserCredentials("username", "password");
        credentialRepository.save(credentials);

        authenticationService.login(credentials);   

        assertThat(credentialRepository.findOne(Example.of(credentials))).isPresent();
        assertThat(credentialRepository.findOne(Example.of(credentials)).get()).isEqualTo(credentials);
    }
    
    @Test
    public void testServiceSendsUserCredentialsThroughRepository(){
        UserCredentials credentials = new UserCredentials("username", "password");
        authenticationService.signup(credentials);

        assertThat(credentialRepository.findOne(Example.of(credentials))).isPresent();
    }

}
