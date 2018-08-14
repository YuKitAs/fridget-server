package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.kit.pse.fridget.server.utilities.Pattern;

@Entity
@Table(name = "devices")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Device {
    @Id
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String id;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String userId;
    private String instanceIdToken;

    public Device() {
    }

    private Device(String id, String userId, String instanceIdToken) {
        this.id = id;
        this.userId = userId;
        this.instanceIdToken = instanceIdToken;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getInstanceIdToken() {
        return instanceIdToken;
    }

    public static Device buildNew(String userId, String instanceIdToken) {
        return new Device(UUID.randomUUID().toString(), userId, instanceIdToken);
    }

    public static Device buildForUpdate(String id, String userId, String instanceIdToken) {
        return new Device(id, userId, instanceIdToken);
    }
}
