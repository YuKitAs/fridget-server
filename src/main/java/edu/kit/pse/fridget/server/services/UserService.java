package edu.kit.pse.fridget.server.services;

import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserWithJwtRepresentation;

public interface UserService {
    UserWithJwtRepresentation registerOrLoginWithIdToken(String googleIdToken);

    UserWithJwtRepresentation registerOrLogin(User user);
}
