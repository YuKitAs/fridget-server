package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
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
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.TaggedMemberRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class CoolNoteServiceTest extends AbstractServiceTest {
    @InjectMocks
    private CoolNoteServiceImpl coolNoteService;
    @Mock
    private CoolNoteRepository coolNoteRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private TaggedMemberRepository taggedMemberRepository;
    @Mock
    private FlatshareRepository flatshareRepository;

    @Before
    public void setUp() throws Exception {
        List<CoolNote> coolNotes = new ArrayList<>();
        coolNotes.add(getFixture("coolNote1.json", CoolNote.class));

        when(coolNoteRepository.findByFlatshareId(FLATSHARE_ID)).thenReturn(coolNotes);
        when(coolNoteRepository.findById(INCORRECT_COOL_NOTE_ID)).thenReturn(Optional.empty());
        when(membershipRepository.findById("00000000-0000-0000-0000-000000000000")).thenReturn(
                Optional.of(getFixture("membership0.json", Membership.class)));
        when(flatshareRepository.findById(INCORRECT_FLATSHARE_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void getAllCoolNotes_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> coolNoteService.getAllCoolNotes(INCORRECT_FLATSHARE_ID)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(FLATSHARE_NOT_FOUND_ERROR_MESSAGE);
    }

    @Test
    public void getCoolNote_WithIncorrectId() {
        assertThatThrownBy(() -> coolNoteService.getCoolNote(INCORRECT_COOL_NOTE_ID)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(COOL_NOTE_NOT_FOUND_ERROR_MESSAGE);
    }

    @Test
    public void saveCoolNote_WithIncorrectCreatorMembershipId() {
        assertThatThrownBy(() -> coolNoteService.saveCoolNote(
                getFixture("coolNoteWithIncorrectCreatorMembershipId.json", CoolNote.class))).isInstanceOf(
                EntityUnprocessableException.class).hasMessage(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }

    @Test
    public void saveCoolNote_WithIncorrectPosition() {
        assertThatThrownBy(
                () -> coolNoteService.saveCoolNote(getFixture("coolNoteWithIncorrectPosition.json", CoolNote.class))).isInstanceOf(
                EntityConflictException.class).hasMessage("Position 0 invalid.");
    }

    @Test
    public void deleteCoolNote_WithIncorrectId() {
        assertThatThrownBy(() -> coolNoteService.deleteCoolNote(INCORRECT_COOL_NOTE_ID)).isInstanceOf(EntityConflictException.class)
                .hasMessage("Cool Note id=\"incorrect-cool-note-id\" cannot be deleted, it does not exist.");
    }
}