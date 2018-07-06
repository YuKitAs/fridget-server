package edu.kit.pse.fridget.server.models.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.pse.fridget.server.models.User;

public class UserWithJwtRepresentation {
    private final User user;
    private final String jwt;

    @JsonCreator
    public UserWithJwtRepresentation(@JsonProperty("user") User user, @JsonProperty("jwt") String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public String getJwt() {
        return jwt;
    }
}
