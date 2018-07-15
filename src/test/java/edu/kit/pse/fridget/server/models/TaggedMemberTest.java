package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class TaggedMemberTest {
    private static final String MEMBERSHIP_ID = "00000000-0000-0000-0000-000000000001";
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void buildNew() {
        TaggedMember taggedMember = TaggedMember.buildNew(MEMBERSHIP_ID, COOL_NOTE_ID);

        assertThat(taggedMember.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(taggedMember.getMembershipId()).isEqualTo(MEMBERSHIP_ID);
        assertThat(taggedMember.getCoolNoteId()).isEqualTo(COOL_NOTE_ID);
    }
}