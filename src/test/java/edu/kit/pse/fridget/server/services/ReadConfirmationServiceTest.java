package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.ReadConfirmationRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReadConfirmationServiceTest extends AbstractServiceTest {
    private static final String INCORRECT_MEMBERSHIP_ID = "incorrect-membership-id";
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

        CoolNote coolNote = getFixture("coolNote0.json", CoolNote.class);

        readConfirmations.add(ReadConfirmation.buildNew(membershipId0, COOL_NOTE_ID));
        readConfirmations.add(ReadConfirmation.buildNew(membershipId1, COOL_NOTE_ID));

        when(readConfirmationRepository.findByCoolNoteId(COOL_NOTE_ID)).thenReturn(Optional.of(readConfirmations));
        when(membershipRepository.findById(membershipId0)).thenReturn(Optional.of(membership0));
        when(membershipRepository.findById(membershipId1)).thenReturn(Optional.of(membership1));
        when(coolNoteRepository.findById(COOL_NOTE_ID)).thenReturn(Optional.of(coolNote));
        when(coolNoteRepository.findById(INCORRECT_COOL_NOTE_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void getAllMemberships() {
        List<Membership> memberships = service.getAllMemberships(COOL_NOTE_ID);

        assertThat(memberships.size()).isEqualTo(2);
        assertThat(memberships.get(0)).isEqualTo(membership0);
        assertThat(memberships.get(1)).isEqualTo(membership1);
    }

    @Test
    public void getAllMemberships_WithIncorrectCoolNoteId() {
        assertThatThrownBy(() -> service.getAllMemberships(INCORRECT_COOL_NOTE_ID)).isInstanceOf(EntityNotFoundException.class);
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

    @Test
    public void saveReadConfirmation_WithIncorrectCoolNoteId() {
        assertThatThrownBy(
                () -> service.saveReadConfirmation(ReadConfirmation.buildNew(membershipId1, INCORRECT_COOL_NOTE_ID))).isInstanceOf(
                EntityUnprocessableException.class);
    }

    @Test
    public void saveReadConfirmation_WithIncorrectMembershipId() {
        assertThatThrownBy(
                () -> service.saveReadConfirmation(ReadConfirmation.buildNew(COOL_NOTE_ID, INCORRECT_MEMBERSHIP_ID))).isInstanceOf(
                EntityUnprocessableException.class);
    }

    @Test
    public void saveReadConfirmation_WithExistedCoolNoteIdAndMembershipId() {
        ReadConfirmation readConfirmation = ReadConfirmation.buildNew(membershipId0, COOL_NOTE_ID);

        when(readConfirmationRepository.findByCoolNoteIdAndMembershipId(COOL_NOTE_ID, membershipId0)).thenReturn(
                Optional.of(readConfirmation));

        assertThatThrownBy(() -> service.saveReadConfirmation(readConfirmation)).isInstanceOf(EntityConflictException.class);
    }

    @Test
    public void deleteReadConfirmation_WithIncorrectCoolNoteId() {
        assertThatThrownBy(() -> service.deleteReadConfirmation(INCORRECT_COOL_NOTE_ID, membershipId1)).isInstanceOf(
                EntityConflictException.class);
    }

    @Test
    public void deleteReadConfirmation_WithIncorrectMembershipId() {
        assertThatThrownBy(() -> service.deleteReadConfirmation(COOL_NOTE_ID, INCORRECT_MEMBERSHIP_ID)).isInstanceOf(
                EntityConflictException.class);
    }
}