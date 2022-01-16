package it.riccardomalavolti.identitas.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Example;
import it.riccardomalavolti.identitas.exceptions.ConflictException;
import it.riccardomalavolti.identitas.exceptions.UnauthorizedException;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.repositories.UserCredentialRepository;
import it.riccardomalavolti.identitas.security.TokenIssuer;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    TokenIssuer tokenService;

    @Mock
    UserCredentialRepository userRepo;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Before
    public void setUp(){
        authenticationService = new AuthenticationService(userRepo, tokenService);
    }

    @Test
    public void loginShouldReturnJWTIfCredentialsAreValid() throws NotFoundException {
        UserCredentials credentials = new UserCredentials("username", "password");
        when(userRepo.exists(Example.of(credentials))).thenReturn(true);
        when(tokenService.issueTokenForUser(credentials.getUsername())).thenReturn("TOKEN");
        String token = authenticationService.login(credentials);

        assertThat(token).isEqualTo("TOKEN");
        verify(userRepo, times(1)).exists(Example.of(credentials));
    }

    @Test(expected = UnauthorizedException.class)
    public void loginShouldThrowsUnathorizedExceptionIfCredentialsAreNotValid() throws Exception {
        UserCredentials credentials = new UserCredentials("username", "password");
        when(userRepo.exists(Example.of(credentials))).thenReturn(false);

        authenticationService.login(credentials);

        verify(userRepo, times(1)).exists(Example.of(credentials));
    }

    @Test
    public void signupShouldCreateNewUser() throws Exception {
        UserCredentials credentials = new UserCredentials("username", "password");
        when(userRepo.exists(Example.of(credentials))).thenReturn(false);

        authenticationService.signup(credentials);

        verify(userRepo, times(1)).save(credentials);
    }

    @Test(expected = ConflictException.class)
    public void signupShouldThrowConflictExceptionWhenUsernameIsTaken() throws Exception {
        UserCredentials credentials = new UserCredentials("username", "password");
        when(userRepo.exists(Example.of(credentials))).thenReturn(true);

        authenticationService.signup(credentials);

        verify(userRepo, times(1)).save(credentials);
    }
    
}
