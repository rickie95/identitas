package it.riccardomalavolti.identitas.security;


import static org.assertj.core.api.Assertions.assertThat;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import io.jsonwebtoken.Jwts;

@RunWith(MockitoJUnitRunner.class)
public class TokenIssuerTest {

    TokenIssuer tokenIssuer;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Before
    public void setup() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        publicKey = (RSAPublicKey) kp.getPublic();
        privateKey = (RSAPrivateKey) kp.getPrivate();

        tokenIssuer = new TokenIssuer(privateKey);
    }

    @Test
    public void shouldIssueAJWT(){
        String username = "username";

        String token = tokenIssuer.issueTokenForUser(username);

        assertThat(Jwts.parserBuilder()
            .setSigningKey(tokenIssuer.privateKey)
            .build().parseClaimsJws(token)
            .getBody().getSubject()).isEqualTo(username);

        Date tokenIssuingDate = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody().getIssuedAt();
        Date tokenExpirationDate = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody().getExpiration();

        assertThat(tokenExpirationDate).isAfter(tokenIssuingDate);
        assertThat(tokenExpirationDate.getTime() - tokenIssuingDate.getTime()).isEqualTo(TokenIssuer.TOKEN_VALIDITY_SECONDS);

    }

}
