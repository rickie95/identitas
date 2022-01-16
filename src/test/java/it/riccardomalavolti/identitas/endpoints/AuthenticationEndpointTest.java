package it.riccardomalavolti.identitas.endpoints;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import it.riccardomalavolti.identitas.exceptions.ConflictException;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.services.AuthenticationService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationEndpointTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  private AuthenticationService authenticationService;

  @Test
  @WithAnonymousUser
  public void shouldLoginAnonymousUsersAndReturnsJWT() throws Exception {
    UserCredentials user = new UserCredentials("mike", "secret");
    ObjectMapper mapper = new ObjectMapper();

    when(authenticationService.login(user)).thenReturn("MY_TOKEN");

    this.mvc
      .perform(post("/authenticate/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(user))
        .characterEncoding("utf-8")
      .accept(MediaType.TEXT_PLAIN))
      .andExpect(status().isOk())
      .andExpect(content().string("MY_TOKEN"));
  }

  @Test
  @WithAnonymousUser
  public void testSignupShouldReturnCreated() throws Exception {
    UserCredentials user = new UserCredentials("mike", "secret");
    ObjectMapper mapper = new ObjectMapper();
    this.mvc
    .perform(post("/authenticate/signup")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user))
      .characterEncoding("utf-8"))
    .andExpect(status().isCreated());

    verify(authenticationService, times(1)).signup(user);
  }

  @Test
  @WithAnonymousUser
  public void testSignupShouldReturnConflictIfUsernameIsAlreadyTaken() throws Exception {
    UserCredentials user = new UserCredentials("mike", "secret");
    ObjectMapper mapper = new ObjectMapper();

    doThrow(new ConflictException()).when(authenticationService).signup(user);

    this.mvc
    .perform(post("/authenticate/signup")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user))
      .characterEncoding("utf-8"))
    .andExpect(status().isConflict());

    verify(authenticationService, times(1)).signup(user);
  }

}
