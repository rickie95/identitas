package it.riccardomalavolti.identitas.security;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

@Service
public class TokenIssuer {

    public final static int TOKEN_VALIDITY_SECONDS = 3600000;

    public RSAPrivateKey privateKey;

    public TokenIssuer(@Value("${jwt.private.key}") RSAPrivateKey privateKey){
        this.privateKey = privateKey;
    }

    public String issueTokenForUser(final String username){
        long issuingTime = System.currentTimeMillis();

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date(issuingTime))
            .setExpiration(new Date(issuingTime + TOKEN_VALIDITY_SECONDS))
            .signWith(privateKey)
            .compact();
    }
    
}
