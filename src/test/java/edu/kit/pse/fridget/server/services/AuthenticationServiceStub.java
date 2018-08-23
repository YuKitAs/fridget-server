package edu.kit.pse.fridget.server.services;

public class AuthenticationServiceStub implements AuthenticationService {
    @Override
    public AuthenticationServiceImpl.GoogleUser verifyIdTokenAndGetPayload(String idTokenString) {
        return new AuthenticationServiceImpl.GoogleUser("a-valid-google-id", "Dummy User");
    }
}
