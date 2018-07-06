package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserWithJwtRepresentation;
import edu.kit.pse.fridget.server.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserServiceImpl(UserRepository repository, AuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
    }

    public UserWithJwtRepresentation registerOrLogin(String googleIdToken) {
        AuthenticationServiceImpl.GoogleUser googleUser = authenticationService.verifyIdTokenAndGetPayload(googleIdToken);

        return repository.findByGoogleUserId(googleUser.getGoogleUserId())
                .map(userFound -> new UserWithJwtRepresentation(userFound, JwtService.encode(userFound.getId())))
                .orElseGet(() -> register(googleUser.getGoogleUserId(), googleUser.getGoogleName()));
    }

    private UserWithJwtRepresentation register(String googleUserId, String googleName) {
        User newUser = User.buildNew(googleUserId, googleName);
        String userId = repository.save(newUser).getId();

        return new UserWithJwtRepresentation(newUser, JwtService.encode(userId));
    }
}
