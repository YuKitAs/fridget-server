package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.kit.pse.fridget.server.utilities.Pattern;

@Entity
@Table(name = "flatshares")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flatshare {
    @Id
    @JsonFormat(pattern = Pattern.UUID_PATTERN)
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String name;

    public Flatshare() {
    }

    private Flatshare(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Flatshare buildNew(String name) {
        return new Flatshare(UUID.randomUUID().toString(), name);
    }
}
