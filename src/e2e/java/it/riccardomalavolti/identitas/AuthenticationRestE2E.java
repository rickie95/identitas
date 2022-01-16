package it.riccardomalavolti.identitas;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.repositories.UserCredentialRepository;
import it.riccardomalavolti.identitas.services.AuthenticationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationRestE2E {

    @Autowired
    UserCredentialRepository credentialRepo;

    @Autowired
    AuthenticationService authService;

    @LocalServerPort
	private int port;

    @Before
    public void setup(){
        RestAssured.port = port;
        credentialRepo.deleteAll();
        credentialRepo.flush();
    }

    @Test
    public void loginShouldReturnJWT() throws JsonProcessingException {
        UserCredentials credentials = new UserCredentials("username", "password");
        credentialRepo.save(credentials);

        ObjectMapper mapper = new ObjectMapper();

        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(mapper.writeValueAsString(credentials)).
        when().
            post("/authenticate/login").
        then().
            assertThat().
                statusCode(200);

    }

    @Test
    public void signupShouldAddTheUser() throws JsonProcessingException {
        UserCredentials credentials = new UserCredentials("username", "password");

        ObjectMapper mapper = new ObjectMapper();

        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(mapper.writeValueAsString(credentials)).
        when().
            post("/authenticate/signup").
        then().
            assertThat().
                statusCode(201);

        assertThat(credentialRepo.exists(Example.of(credentials))).isTrue();
    }

    
}
