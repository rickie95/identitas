package it.riccardomalavolti.identitas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import it.riccardomalavolti.identitas.exceptions.ConflictException;
import it.riccardomalavolti.identitas.exceptions.UnauthorizedException;
import it.riccardomalavolti.identitas.model.UserCredentials;
import it.riccardomalavolti.identitas.repositories.UserCredentialRepository;
import it.riccardomalavolti.identitas.security.TokenIssuer;

@Service
public class AuthenticationService {

    private TokenIssuer tokenIssuer;
    private UserCredentialRepository userRepo;

    @Autowired
    public AuthenticationService(UserCredentialRepository userRepository, TokenIssuer tokenIssuer){
        this.userRepo = userRepository;
        this.tokenIssuer = tokenIssuer;
    }

    /** If the credentials are valid, issues a JWT. Otherwise throws an UnauthorizedException.
     * @param credentials
     * @return a JWT token as a String
     */
    public String login(UserCredentials credentials) {
        if(!userRepo.exists(Example.of(credentials)))
            throw new UnauthorizedException();
     
        return tokenIssuer.issueTokenForUser(credentials.getUsername());
    }

    public void signup(UserCredentials credentials) {
        if(userRepo.exists(Example.of(credentials)))
            throw new ConflictException();

        userRepo.save(credentials);
    }
    
}
