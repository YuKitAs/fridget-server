package edu.kit.pse.fridget.server.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import edu.kit.pse.fridget.server.exceptions.UnauthorizedException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    public GoogleUser verifyIdTokenAndGetPayload(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory()).build();

        try {
            GoogleIdToken.Payload payload = Optional.ofNullable(verifier.verify(idTokenString))
                    .orElseThrow(UnauthorizedException::buildForBrokenIdToken)
                    .getPayload();
            return new GoogleUser(payload.getSubject(), (String) payload.get("name"));
        } catch (GeneralSecurityException e) {
            throw UnauthorizedException.buildForBrokenIdToken();
        } catch (IOException e) {
            throw UnauthorizedException.buildForConnectionProblem();
        }
    }

    static class GoogleUser {
        private String googleUserId;
        private String googleName;

        GoogleUser(String googleUserId, String googleName) {
            this.googleUserId = googleUserId;
            this.googleName = googleName;
        }

        String getGoogleUserId() {
            return googleUserId;
        }

        String getGoogleName() {
            return googleName;
        }
    }
}
