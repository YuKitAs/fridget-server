package edu.kit.pse.fridget.server.models.commands;

import org.junit.Test;

import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class SaveFlatshareCommandTest {
    @Test
    public void buildFlatshare() {
        String flatshareName = "dummy-flashare";
        Flatshare flatshare = new SaveFlatshareCommand("00000000-0000-0000-0000-000000000000", flatshareName).buildFlatshare();

        assertThat(flatshare.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(flatshare.getName()).matches(flatshareName);
    }
}