package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.ReadConfirmationRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadConfirmationServiceTest {
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String USER_ID_0 = "00000000-0000-0000-0000-000000000000";
    private static final String USER_ID_1 = "00000000-0000-0000-0000-000000000001";
    private static final String MAGNET_COLOR_0 = "0099cc";
    private static final String MAGNET_COLOR_1 = "ffffff";
    @InjectMocks
    private ReadConfirmationServiceImpl service;
    @Mock
    private ReadConfirmationRepository readConfirmationRepository;
    @Mock
    private MembershipRepository membershipRepository;

    private String membershipId0;
    private String membershipId1;
    private List<ReadConfirmation> readConfirmations = new ArrayList<>();

    @Before
    public void setUp() {
        Membership membership0 = new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId(USER_ID_0)
                .setMagnetColor(MAGNET_COLOR_0)
                .build();
        Membership membership1 = new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId(USER_ID_1)
                .setMagnetColor(MAGNET_COLOR_1)
                .build();
        membershipId0 = membership0.getId();
        membershipId1 = membership1.getId();

        readConfirmations.add(ReadConfirmation.buildNew(membershipId0, COOL_NOTE_ID));
        readConfirmations.add(ReadConfirmation.buildNew(membershipId1, COOL_NOTE_ID));

        when(readConfirmationRepository.findByCoolNoteId(COOL_NOTE_ID)).thenReturn(readConfirmations);
        when(membershipRepository.getOne(membershipId0)).thenReturn(membership0);
        when(membershipRepository.getOne(membershipId1)).thenReturn(membership1);
    }

    @Test
    public void getAllMemberships() {
        List<Membership> memberships = service.getAllMemberships(COOL_NOTE_ID);

        assertThat(memberships.size()).isEqualTo(2);

        Membership membership0 = memberships.get(0);
        Membership membership1 = memberships.get(1);

        assertThat(membership0.getId()).isEqualTo(membershipId0);
        assertThat(membership0.getFlatshareId()).isEqualTo(FLATSHARE_ID);
        assertThat(membership0.getUserId()).isEqualTo(USER_ID_0);
        assertThat(membership0.getMagnetColor()).isEqualTo(MAGNET_COLOR_0);
        assertThat(membership1.getId()).isEqualTo(membershipId1);
        assertThat(membership1.getFlatshareId()).isEqualTo(FLATSHARE_ID);
        assertThat(membership1.getUserId()).isEqualTo(USER_ID_1);
        assertThat(membership1.getMagnetColor()).isEqualTo(MAGNET_COLOR_1);
    }

    @Test
    public void saveReadConfirmation() {
        service.saveReadConfirmation(ReadConfirmation.buildNew(membershipId1, COOL_NOTE_ID));

        ArgumentCaptor<ReadConfirmation> readConfirmationArgumentCaptor = ArgumentCaptor.forClass(ReadConfirmation.class);
        verify(readConfirmationRepository).save(readConfirmationArgumentCaptor.capture());

        ReadConfirmation readConfirmationToSave = readConfirmationArgumentCaptor.getValue();
        assertThat(readConfirmationToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(readConfirmationToSave.getMembershipId()).isEqualTo(membershipId1);
        assertThat(readConfirmationToSave.getCoolNoteId()).isEqualTo(COOL_NOTE_ID);
    }
}