package edu.kit.pse.fridget.server.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class CoolNoteTest {
    private CoolNote coolNoteForCreation;
    private static final String TAGGED_MEMBER_ID_0 = "00000000-0000-0000-0000-000000000000";
    private static final String TAGGED_MEMBER_ID_1 = "00000000-0000-0000-0000-000000000001";

    @Before
    public void setUp() throws IOException {
        coolNoteForCreation = CoolNote.buildForCreation(
                new ObjectMapper().readValue(new File("src/test/resources/fixtures/coolNote0.json"), CoolNote.class));
    }

    @Test
    public void buildForCreation() {
        assertThat(coolNoteForCreation.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(coolNoteForCreation.getTitle()).isEqualTo("Goodbye");
        assertThat(coolNoteForCreation.getContent()).isEqualTo("Goodbye world!");
        assertThat(coolNoteForCreation.getImportance()).isEqualTo(0);
        assertThat(coolNoteForCreation.getPosition()).isEqualTo(1);
        assertThat(coolNoteForCreation.getCreatedAt()).isNotNull();
        assertThat(coolNoteForCreation.getTaggedMembershipIds()).isEmpty();
    }

    @Test
    public void buildForFetching() {
        List<String> taggedMembershipIds = new ArrayList<>();
        taggedMembershipIds.add(TAGGED_MEMBER_ID_0);
        taggedMembershipIds.add(TAGGED_MEMBER_ID_1);
        CoolNote coolNoteForFetching = CoolNote.buildForFetching(coolNoteForCreation, taggedMembershipIds);

        assertThat(coolNoteForFetching.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(coolNoteForFetching.getTitle()).isEqualTo("Goodbye");
        assertThat(coolNoteForFetching.getContent()).isEqualTo("Goodbye world!");
        assertThat(coolNoteForFetching.getImportance()).isEqualTo(0);
        assertThat(coolNoteForFetching.getPosition()).isEqualTo(1);
        assertThat(coolNoteForFetching.getCreatedAt()).isNotNull();
        assertThat(coolNoteForFetching.getTaggedMembershipIds().size()).isEqualTo(2);
        assertThat(coolNoteForFetching.getTaggedMembershipIds()).contains(TAGGED_MEMBER_ID_0, TAGGED_MEMBER_ID_1);
    }
}