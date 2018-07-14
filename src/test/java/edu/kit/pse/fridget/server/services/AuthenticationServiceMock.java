package edu.kit.pse.fridget.server.services;

public class AuthenticationServiceMock implements AuthenticationService {
    @Override
    public AuthenticationServiceImpl.GoogleUser verifyIdTokenAndGetPayload(String idTokenString) {
        return new AuthenticationServiceImpl.GoogleUser("a-valid-google-id", "Dummy User");
    }
}
