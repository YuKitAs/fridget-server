package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.FrozenNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlatshareServiceTest extends AbstractServiceTest {
    private static final String FLATSHARE_NAME = "An Awesome Flatshare";
    @InjectMocks
    private FlatshareServiceImpl flatshareService;
    @Mock
    private FlatshareRepository flatshareRepository;
    @Mock
    private UserRepository userRepository;
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

        when(userRepository.findById(USER_ID_0)).thenReturn(Optional.of(User.buildNew("dummy-id", "dummy-name")));
        when(userRepository.findById(INCORRECT_USER_ID)).thenReturn(Optional.empty());
        when(flatshareRepository.findById(INCORRECT_FLATSHARE_ID)).thenReturn(Optional.empty());
        when(flatshareRepository.save(createdFlatshare)).thenReturn(createdFlatshare);
        when(magnetColorService.getRandomColor()).thenReturn(MAGNET_COLOR_0);
    }

    @Test
    public void getFlatshare_WithIncorrectId() {
        assertThatThrownBy(() -> flatshareService.getFlatshare(INCORRECT_FLATSHARE_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void saveFlatshare() {
        flatshareService.saveFlatshare(createdFlatshare, USER_ID_0);

        ArgumentCaptor<Membership> membershipArgumentCaptor = ArgumentCaptor.forClass(Membership.class);
        verify(membershipService).saveMembership(membershipArgumentCaptor.capture());

        Membership membershipToSave = membershipArgumentCaptor.getValue();
        assertThat(membershipToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(membershipToSave.getUserId()).isEqualTo(USER_ID_0);
        assertThat(membershipToSave.getFlatshareId()).isEqualTo(createdFlatshare.getId());
        assertThat(membershipToSave.getMagnetColor()).isEqualTo(MAGNET_COLOR_0);

        ArgumentCaptor<FrozenNote> frozenNoteArgumentCaptor = ArgumentCaptor.forClass(FrozenNote.class);
        verify(frozenNoteService, times(3)).saveFrozenNote(frozenNoteArgumentCaptor.capture());

        FrozenNote frozenNoteToSave = frozenNoteArgumentCaptor.getValue();
        assertThat(frozenNoteToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(frozenNoteToSave.getFlatshareId()).isEqualTo(createdFlatshare.getId());
        assertThat(frozenNoteToSave.getTitle()).isBlank();
        assertThat(frozenNoteToSave.getContent()).isBlank();
        assertThat(frozenNoteToSave.getPosition()).isBetween(0, 2);
    }

    @Test
    public void saveFlatshare_WithIncorrectUserId() {
        assertThatThrownBy(() -> flatshareService.saveFlatshare(createdFlatshare, INCORRECT_USER_ID)).isInstanceOf(
                EntityUnprocessableException.class);
    }
}