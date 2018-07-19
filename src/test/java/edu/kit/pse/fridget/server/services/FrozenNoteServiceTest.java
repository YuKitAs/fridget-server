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
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.FrozenNote;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.FrozenNoteRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class FrozenNoteServiceTest extends AbstractServiceTest {
    private static final String FROZEN_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String INCORRECT_FROZEN_NOTE_ID = "incorrect-frozen-note-id";
    @InjectMocks
    private FrozenNoteServiceImpl service;
    @Mock
    private FrozenNoteRepository frozenNoteRepository;
    @Mock
    private FlatshareRepository flatshareRepository;

    @Before
    public void setUp() throws Exception {
        List<FrozenNote> frozenNotes = new ArrayList<>();
        FrozenNote frozenNote = getFixture("frozenNote.json", FrozenNote.class);
        frozenNotes.add(frozenNote);

        when(frozenNoteRepository.findByFlatshareId(INCORRECT_FLATSHARE_ID)).thenReturn(Optional.empty());
        when(frozenNoteRepository.findById(FROZEN_NOTE_ID)).thenReturn(Optional.of(frozenNote));
        when(frozenNoteRepository.findById(INCORRECT_FROZEN_NOTE_ID)).thenReturn(Optional.empty());
        when(flatshareRepository.findById(FLATSHARE_ID)).thenReturn(Optional.of(Flatshare.buildNew("dummy-flatshare")));
    }

    @Test
    public void getAllFrozenNotes_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> service.getAllFrozenNotes(INCORRECT_FLATSHARE_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void getFrozenNote_WithIncorrectId() {
        assertThatThrownBy(() -> service.getFrozenNote(INCORRECT_FROZEN_NOTE_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void updateFrozenNote_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> service.updateFrozenNote(FROZEN_NOTE_ID,
                getFixture("frozenNoteWithIncorrectFlatshareId.json", FrozenNote.class))).isInstanceOf(EntityUnprocessableException.class);
    }

    @Test
    public void updateFrozenNote_WithIncorrectPosition() {
        assertThatThrownBy(() -> service.updateFrozenNote(FROZEN_NOTE_ID,
                getFixture("frozenNoteWithIncorrectPosition.json", FrozenNote.class))).isInstanceOf(EntityConflictException.class);
    }
}