package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.kit.pse.fridget.server.utilities.Pattern;

@Entity
@Table(name = "read_confirmations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReadConfirmation {
    @Id
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String id;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String membershipId;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String coolNoteId;

    public ReadConfirmation() {
    }

    private ReadConfirmation(String id, String membershipId, String coolNoteId) {
        this.id = id;
        this.membershipId = membershipId;
        this.coolNoteId = coolNoteId;
    }

    public String getId() {
        return id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getCoolNoteId() {
        return coolNoteId;
    }

    public static ReadConfirmation buildNew(String membershipId, String coolNoteId) {
        return new ReadConfirmation(UUID.randomUUID().toString(), membershipId, coolNoteId);
    }
}
