package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatshareTest {
    private static final String FLATSHARE_NAME = "An Awesome Flatshare";

    @Test
    public void buildNew() {
        Flatshare flatshare = Flatshare.buildNew(FLATSHARE_NAME);

        assertThat(flatshare.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(flatshare.getName()).matches(FLATSHARE_NAME);
    }
}