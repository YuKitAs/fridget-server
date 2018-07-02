package edu.kit.pse.fridget.server.models.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;

public class UserMembershipRepresentation {
    private final String membershipId;
    private final String magnetColor;
    private final String googleName;

    @JsonCreator
    private UserMembershipRepresentation(@JsonProperty("membershipId") String membershipId, @JsonProperty("magnetColor") String magnetColor,
            @JsonProperty("googleName") String googleName) {
        this.membershipId = membershipId;
        this.magnetColor = magnetColor;
        this.googleName = googleName;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getMagnetColor() {
        return magnetColor;
    }

    public String getGoogleName() {
        return googleName;
    }

    public static UserMembershipRepresentation buildFromUserAndMembership(User user, Membership membership) {
        return new UserMembershipRepresentation(membership.getId(), membership.getMagnetColor(), user.getGoogleName());
    }
}
