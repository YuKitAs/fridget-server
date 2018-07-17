package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MagnetColorServiceTest extends AbstractServiceTest {
    @InjectMocks
    private MagnetColorService service;
    @Mock
    private MembershipRepository membershipRepository;

    @Before
    public void setUp() {
        List<Membership> memberships = new ArrayList<>();

        memberships.add(new Membership.Builder().setRandomId().setFlatshareId(FLATSHARE_ID).setUserId(USER_ID_0)
                .setMagnetColor(MAGNET_COLOR_0)
                .build());
        memberships.add(new Membership.Builder().setRandomId().setFlatshareId(FLATSHARE_ID).setUserId(USER_ID_1)
                .setMagnetColor(MAGNET_COLOR_1)
                .build());
        memberships.add(new Membership.Builder().setRandomId().setFlatshareId(FLATSHARE_ID).setUserId(USER_ID_2)
                .setMagnetColor(MAGNET_COLOR_2)
                .build());

        when(membershipRepository.findByFlatshareId(FLATSHARE_ID)).thenReturn(Optional.of(memberships));
    }

    @Test
    public void getRandomColor() {
        assertThat(service.getRandomColor()).matches(Pattern.HEX_COLOR_CODE_PATTERN);
    }

    @Test
    public void getAvailableRandomColor() {
        assertThat(service.getAvailableRandomColor(FLATSHARE_ID)).isNotEqualTo(MAGNET_COLOR_0)
                .isNotEqualTo(MAGNET_COLOR_1)
                .isNotEqualTo(MAGNET_COLOR_2);
    }
}