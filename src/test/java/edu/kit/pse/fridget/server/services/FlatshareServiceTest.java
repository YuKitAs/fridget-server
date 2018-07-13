package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.FrozenNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlatshareServiceTest {
    private static final String FLATSHARE_NAME = "An Awesome Flatshare";
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MAGNET_COLOR = "0099cc";
    @InjectMocks
    private FlatshareServiceImpl flatshareService;
    @Mock
    private FlatshareRepository repository;
    @Mock
    private MembershipService membershipService;
    @Mock
    private FrozenNoteService frozenNoteService;
    @Mock
    private MagnetColorService magnetColorService;

    private Flatshare createdFlatshare;

    @Before
    public void setUp() {
        createdFlatshare = Flatshare.buildNew(FLATSHARE_NAME);

        when(repository.save(createdFlatshare)).thenReturn(createdFlatshare);
        when(magnetColorService.getRandomColor()).thenReturn(MAGNET_COLOR);
    }

    @Test
    public void saveFlatshare_WithCorrectMembershipAndFrozenNotes() {
        flatshareService.saveFlatshare(createdFlatshare, USER_ID);

        ArgumentCaptor<Membership> membershipArgumentCaptor = ArgumentCaptor.forClass(Membership.class);
        verify(membershipService).saveMembership(membershipArgumentCaptor.capture());

        Membership membershipToSave = membershipArgumentCaptor.getValue();
        assertThat(membershipToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(membershipToSave.getUserId()).isEqualTo(USER_ID);
        assertThat(membershipToSave.getFlatshareId()).isEqualTo(createdFlatshare.getId());
        assertThat(membershipToSave.getMagnetColor()).isEqualTo(MAGNET_COLOR);

        ArgumentCaptor<FrozenNote> frozenNoteArgumentCaptor = ArgumentCaptor.forClass(FrozenNote.class);
        verify(frozenNoteService, times(3)).saveFrozenNote(frozenNoteArgumentCaptor.capture());

        FrozenNote frozenNoteToSave = frozenNoteArgumentCaptor.getValue();
        assertThat(frozenNoteToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(frozenNoteToSave.getFlatshareId()).isEqualTo(createdFlatshare.getId());
        assertThat(frozenNoteToSave.getTitle()).isBlank();
        assertThat(frozenNoteToSave.getContent()).isBlank();
        assertThat(frozenNoteToSave.getPosition()).isBetween(0, 2);
    }
}