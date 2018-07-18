package edu.kit.pse.fridget.server.models.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.utilities.Pattern;

public class SaveMembershipCommand {
    @JsonFormat(pattern = Pattern.ACCESS_CODE_PATTERN)
    private final String accessCode;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private final String userId;

    @JsonCreator
    public SaveMembershipCommand(@JsonProperty("accessCode") String accessCode, @JsonProperty("userId") String userId) {
        this.accessCode = accessCode;
        this.userId = userId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getUserId() {
        return userId;
    }

    @JsonIgnore
    public Membership.Builder getBuilder() {
        return new Membership.Builder();
    }
}
