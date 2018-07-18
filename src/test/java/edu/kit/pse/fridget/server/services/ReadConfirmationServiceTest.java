package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.ReadConfirmationRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReadConfirmationServiceTest extends AbstractServiceTest {
    @InjectMocks
    private ReadConfirmationServiceImpl service;
    @Mock
    private ReadConfirmationRepository readConfirmationRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private CoolNoteRepository coolNoteRepository;

    private Membership membership0;
    private Membership membership1;
    private String membershipId0;
    private String membershipId1;
    private List<ReadConfirmation> readConfirmations = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        membership0 = getFixture("membership0.json", Membership.class);
        membership1 = getFixture("membership1.json", Membership.class);
        membershipId0 = membership0.getId();
        membershipId1 = membership1.getId();

        CoolNote coolNote = getFixture("coolNote.json", CoolNote.class);

        readConfirmations.add(ReadConfirmation.buildNew(membershipId0, COOL_NOTE_ID));
        readConfirmations.add(ReadConfirmation.buildNew(membershipId1, COOL_NOTE_ID));

        when(readConfirmationRepository.findByCoolNoteId(COOL_NOTE_ID)).thenReturn(Optional.of(readConfirmations));
        when(membershipRepository.findById(membershipId0)).thenReturn(Optional.of(membership0));
        when(membershipRepository.findById(membershipId1)).thenReturn(Optional.of(membership1));
        when(coolNoteRepository.findById(COOL_NOTE_ID)).thenReturn(Optional.of(coolNote));
    }

    @Test
    public void getAllMemberships() {
        List<Membership> memberships = service.getAllMemberships(COOL_NOTE_ID);

        assertThat(memberships.size()).isEqualTo(2);
        assertThat(memberships.get(0)).isEqualTo(membership0);
        assertThat(memberships.get(1)).isEqualTo(membership1);
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