package it.riccardomalavolti.identitas;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.repositories.UserCredentialRepository;
import it.riccardomalavolti.identitas.services.AuthenticationService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationEndpointServiceIT {

    @MockBean
    private UserCredentialRepository credentialRepository;


    @InjectMocks
	private AuthenticationService authenticationService;

	@LocalServerPort
	private int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}

    @Test
    public void testLoginShouldReturnsTheJWTForgedByAuthService() throws Exception {
        UserCredentials credentials = new UserCredentials("username", "password");
        when(credentialRepository.exists(Example.of(credentials))).thenReturn(true);
        
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(credentials).
            when().
                post("/authenticate/login").
            then().
                assertThat().
                    statusCode(200);

        verify(credentialRepository, times(1)).exists(Example.of(credentials));
    }

    @Test
    public void testSignupShouldSaveCredentialsInRepository(){
        UserCredentials credentials = new UserCredentials("username", "password");
        when(credentialRepository.exists(Example.of(credentials))).thenReturn(false);

        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(credentials).
        when().log().all().
            post("/authenticate/signup").
        then().log().all().
            assertThat().
                statusCode(201);

        verify(credentialRepository, times(1)).save(credentials);
    }
}
