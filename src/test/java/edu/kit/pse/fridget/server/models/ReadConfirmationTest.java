package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadConfirmationTest {
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MEMBERSHIP_ID = "00000000-0000-0000-0000-000000000001";

    @Test
    public void buildNew() {
        ReadConfirmation readConfirmation = ReadConfirmation.buildNew(MEMBERSHIP_ID, COOL_NOTE_ID);

        assertThat(readConfirmation.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(readConfirmation.getCoolNoteId()).isEqualTo(COOL_NOTE_ID);
        assertThat(readConfirmation.getMembershipId()).isEqualTo(MEMBERSHIP_ID);
    }
}