package pl.edu.pw.odasprojekt.services;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS512.key().build();
    private static final int EXPIRE_TIME = 10 * 60 * 1000; // 10 minutes
    private static final String ISSUER = "nBank";

    public String generateJwtForUser(String clientNumber) {
        var currentDate = new Date();
        var expireDate = new Date(currentDate.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .subject(clientNumber)
                .issuer(ISSUER)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(jwt);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getClientNumberFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
