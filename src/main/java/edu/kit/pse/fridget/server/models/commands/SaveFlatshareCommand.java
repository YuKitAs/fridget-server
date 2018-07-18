package edu.kit.pse.fridget.server.models.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.utilities.Pattern;

public class SaveFlatshareCommand {
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private final String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final String flatshareName;

    @JsonCreator
    public SaveFlatshareCommand(@JsonProperty("userId") String userId, @JsonProperty("flatshareName") String flatshareName) {
        this.userId = userId;
        this.flatshareName = flatshareName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFlatshareName() {
        return flatshareName;
    }

    public Flatshare buildFlatshare() {
        return Flatshare.buildNew(flatshareName);
    }
}
