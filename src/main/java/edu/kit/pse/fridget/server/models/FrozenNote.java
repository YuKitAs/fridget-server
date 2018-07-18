package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.kit.pse.fridget.server.utilities.Pattern;

@Entity
@Table(name = "frozen_notes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FrozenNote {
    @Id
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String id;
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String flatshareId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private int position;

    public FrozenNote() {
    }

    public FrozenNote(String id, String flatshareId, String title, String content, int position) {
        this.id = id;
        this.flatshareId = flatshareId;
        this.title = title;
        this.content = content;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFlatshareId() {
        return flatshareId;
    }

    public int getPosition() {
        return position;
    }
}
