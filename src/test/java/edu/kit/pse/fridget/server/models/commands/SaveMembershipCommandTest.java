package edu.kit.pse.fridget.server.models.commands;

import org.junit.Test;

import edu.kit.pse.fridget.server.models.Membership;

import static org.assertj.core.api.Assertions.assertThat;

public class SaveMembershipCommandTest {
    @Test
    public void getBuilder() {
        assertThat(new SaveMembershipCommand("abc42", "00000000-0000-0000-0000-000000000000").getBuilder()).isInstanceOf(
                Membership.Builder.class);
    }
}