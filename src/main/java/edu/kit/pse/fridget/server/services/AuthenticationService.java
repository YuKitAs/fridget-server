package edu.kit.pse.fridget.server.services;

public interface AuthenticationService {
    AuthenticationServiceImpl.GoogleUser verifyIdTokenAndGetPayload(String idTokenString);
}
