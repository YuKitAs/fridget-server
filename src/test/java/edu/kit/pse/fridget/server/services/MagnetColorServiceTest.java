package edu.kit.pse.fridget.server.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MagnetColorServiceTest {
    @InjectMocks
    private MagnetColorService service;
    @Mock
    private MembershipRepository membershipRepository;

    @Test
    public void getRandomColor() {
        assertThat(service.getRandomColor()).matches(Pattern.HEX_COLOR_CODE_PATTERN);
    }
}