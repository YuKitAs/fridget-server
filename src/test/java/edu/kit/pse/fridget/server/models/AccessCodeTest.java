package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeTest {
    private static final String CONTENT = "abc42";
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void buildNew() {
        AccessCode accessCode = AccessCode.buildNew(CONTENT, FLATSHARE_ID);

        assertThat(accessCode.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(accessCode.getContent()).isEqualTo(CONTENT);
        assertThat(accessCode.getFlatshareId()).isEqualTo(FLATSHARE_ID);
    }
}