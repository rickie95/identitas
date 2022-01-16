package it.riccardomalavolti.identitas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.services.AuthenticationService;

@RestController
@RequestMapping(AuthenticationEndpoint.PATH)
public class AuthenticationEndpoint {

    public static final String PATH = "/authenticate";
    public static final String LOGIN_PATH = "login";
    public static final String SIGNUP_PATH = "signup";

    AuthenticationService authenticationService;

    @Autowired
    AuthenticationEndpoint(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }
    
    /** 
     * Login answers with a 200 if user is authenticated, 
     * and returns a JWT. Otherwise answers with a 401.
    */
    @PostMapping(LOGIN_PATH)
    public ResponseEntity<String> login(@RequestBody UserCredentials credentials){
        return ResponseEntity.ok().body(authenticationService.login(credentials));
    }

    /** 
     * Signup responds with a 201 for successful logins, 
     * 409 if the username already exist.
     */
    @PostMapping(SIGNUP_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody UserCredentials credentials){
        authenticationService.signup(credentials);
    }

}
