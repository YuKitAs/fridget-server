package edu.kit.pse.fridget.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import edu.kit.pse.fridget.server.exceptions.UnauthorizedException;

public class JwtService {
    private static Algorithm algorithm = Algorithm.HMAC256("secret");

    public static String encode(String userId) {
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(algorithm);
    }

    public static String decode(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(5).build();
            DecodedJWT jwt = verifier.verify(token);

            return jwt.getClaim("userId").asString();
        } catch (JWTVerificationException exception) {
            throw UnauthorizedException.buildForBrokenJwt();
        }
    }
}
