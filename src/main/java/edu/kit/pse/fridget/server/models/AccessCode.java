package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.kit.pse.fridget.server.utilities.Pattern;

@Entity
@Table(name = "access_codes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccessCode {
    @Id
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String id;
    @JsonFormat(pattern = Pattern.ACCESS_CODE_PATTERN)
    private String content;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String flatshareId;

    public AccessCode() {
    }

    private AccessCode(String id, String content, String flatshareId) {
        this.id = id;
        this.content = content;
        this.flatshareId = flatshareId;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getFlatshareId() {
        return flatshareId;
    }

    public static AccessCode buildNew(String content, String flatshareId) {
        return new AccessCode(UUID.randomUUID().toString(), content, flatshareId);
    }
}
