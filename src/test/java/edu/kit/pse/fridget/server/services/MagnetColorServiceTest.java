package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MagnetColorServiceTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MAGNET_COLOR_0 = "6ddbff";
    private static final String MAGNET_COLOR_1 = "ffffff";
    private static final String MAGNET_COLOR_2 = "0054ff";
    @InjectMocks
    private MagnetColorService service;
    @Mock
    private MembershipRepository membershipRepository;

    @Before
    public void setUp() {
        List<Membership> memberships = new ArrayList<>();

        memberships.add(new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId("00000000-0000-0000-0000-000000000000")
                .setMagnetColor(MAGNET_COLOR_0)
                .build());
        memberships.add(new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId("00000000-0000-0000-0000-000000000001")
                .setMagnetColor(MAGNET_COLOR_1)
                .build());
        memberships.add(new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId("00000000-0000-0000-0000-000000000002")
                .setMagnetColor(MAGNET_COLOR_2)
                .build());

        when(membershipRepository.findByFlatshareId(FLATSHARE_ID)).thenReturn(memberships);
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