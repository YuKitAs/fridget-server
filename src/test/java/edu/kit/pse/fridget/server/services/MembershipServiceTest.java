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
import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserMembershipRepresentation;
import edu.kit.pse.fridget.server.repositories.AccessCodeRepository;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MembershipServiceTest extends AbstractServiceTest {
    private static final String GOOGLE_USER_ID_0 = "valid-google-id-0";
    private static final String GOOGLE_USER_ID_1 = "valid-google-id-1";
    private static final String GOOGLE_NAME_0 = "John Doe";
    private static final String GOOGLE_NAME_1 = "Jane Doe";
    private static final String ACCESS_CODE_CONTENT = "abc42";
    private static final String INCORRECT_ACCESS_CODE_CONTENT = "incorrect-access-code-content";
    @InjectMocks
    private MembershipServiceImpl membershipService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FlatshareRepository flatshareRepository;
    @Mock
    private AccessCodeRepository accessCodeRepository;
    @Mock
    private MagnetColorService magnetColorService;

    private String userId0;
    private String userId1;
    private String membershipId0;
    private String membershipId1;

    @Before
    public void setUp() {
        User user0 = User.buildNew(GOOGLE_USER_ID_0, GOOGLE_NAME_0);
        User user1 = User.buildNew(GOOGLE_USER_ID_1, GOOGLE_NAME_1);
        userId0 = user0.getId();
        userId1 = user1.getId();

        Membership membership0 = new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId(userId0)
                .setMagnetColor(MAGNET_COLOR_0)
                .build();
        Membership membership1 = new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId(userId1)
                .setMagnetColor(MAGNET_COLOR_1)
                .build();
        membershipId0 = membership0.getId();
        membershipId1 = membership1.getId();

        List<Membership> memberships = new ArrayList<>();
        memberships.add(membership0);
        memberships.add(membership1);

        when(membershipRepository.findByFlatshareId(FLATSHARE_ID)).thenReturn(Optional.of(memberships));
        when(membershipRepository.findByFlatshareId(INCORRECT_FLATSHARE_ID)).thenReturn(Optional.empty());
        when(membershipRepository.findByFlatshareIdAndUserId(FLATSHARE_ID, userId0)).thenReturn(Optional.of(membership0));
        when(membershipRepository.findByFlatshareIdAndUserId(FLATSHARE_ID, INCORRECT_USER_ID)).thenReturn(Optional.empty());
        when(membershipRepository.findByFlatshareIdAndUserId(INCORRECT_FLATSHARE_ID, userId0)).thenReturn(Optional.empty());
        when(userRepository.findById(userId0)).thenReturn(Optional.of(user0));
        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(INCORRECT_USER_ID)).thenReturn(Optional.empty());
        when(flatshareRepository.findById(FLATSHARE_ID)).thenReturn(Optional.of(Flatshare.buildNew("dummy-flatshare")));
        when(accessCodeRepository.findByContent(ACCESS_CODE_CONTENT)).thenReturn(
                Optional.of(AccessCode.buildNew(ACCESS_CODE_CONTENT, FLATSHARE_ID)));
        when(accessCodeRepository.findByContent(INCORRECT_ACCESS_CODE_CONTENT)).thenReturn(Optional.empty());
        when(magnetColorService.getAvailableRandomColor(FLATSHARE_ID)).thenReturn(MAGNET_COLOR_1);
    }

    @Test
    public void getAllMembers() {
        List<UserMembershipRepresentation> userMembershipRepresentations = membershipService.getAllMembers(FLATSHARE_ID);

        assertThat(userMembershipRepresentations.size()).isEqualTo(2);

        UserMembershipRepresentation userMembershipRepresentation0 = userMembershipRepresentations.get(0);
        UserMembershipRepresentation userMembershipRepresentation1 = userMembershipRepresentations.get(1);

        assertThat(userMembershipRepresentation0.getMembershipId()).isEqualTo(membershipId0);
        assertThat(userMembershipRepresentation0.getGoogleName()).isEqualTo(GOOGLE_NAME_0);
        assertThat(userMembershipRepresentation0.getMagnetColor()).isEqualTo(MAGNET_COLOR_0);
        assertThat(userMembershipRepresentation1.getMembershipId()).isEqualTo(membershipId1);
        assertThat(userMembershipRepresentation1.getGoogleName()).isEqualTo(GOOGLE_NAME_1);
        assertThat(userMembershipRepresentation1.getMagnetColor()).isEqualTo(MAGNET_COLOR_1);
    }

    @Test
    public void getAllMembers_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> membershipService.getAllMembers(INCORRECT_FLATSHARE_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void getMember() {
        UserMembershipRepresentation userMembershipRepresentation = membershipService.getMember(FLATSHARE_ID, userId0);

        assertThat(userMembershipRepresentation.getMembershipId()).isEqualTo(membershipId0);
        assertThat(userMembershipRepresentation.getGoogleName()).isEqualTo(GOOGLE_NAME_0);
        assertThat(userMembershipRepresentation.getMagnetColor()).isEqualTo(MAGNET_COLOR_0);
    }

    @Test
    public void getMember_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> membershipService.getMember(INCORRECT_FLATSHARE_ID, userId0)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void getMember_WithIncorrectUserId() {
        assertThatThrownBy(() -> membershipService.getMember(FLATSHARE_ID, INCORRECT_USER_ID)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void saveMembership_AccordingToAccessCode() {
        membershipService.saveMembership(ACCESS_CODE_CONTENT, userId1, new Membership.Builder());

        ArgumentCaptor<Membership> membershipArgumentCaptor = ArgumentCaptor.forClass(Membership.class);
        verify(membershipRepository).save(membershipArgumentCaptor.capture());

        Membership membershipToSave = membershipArgumentCaptor.getValue();
        assertThat(membershipToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(membershipToSave.getFlatshareId()).isEqualTo(FLATSHARE_ID);
        assertThat(membershipToSave.getUserId()).isEqualTo(userId1);
        assertThat(membershipToSave.getMagnetColor()).isEqualTo(MAGNET_COLOR_1);
    }

    @Test
    public void saveMembership_WithIncorrectAccessCode() {
        assertThatThrownBy(
                () -> membershipService.saveMembership(INCORRECT_ACCESS_CODE_CONTENT, userId1, new Membership.Builder())).isInstanceOf(
                EntityNotFoundException.class);
    }

    @Test
    public void saveMembership_WithIncorrectUserId() {
        assertThatThrownBy(
                () -> membershipService.saveMembership(ACCESS_CODE_CONTENT, INCORRECT_USER_ID, new Membership.Builder())).isInstanceOf(
                EntityUnprocessableException.class);
    }

    @Test
    public void deleteMembership_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> membershipService.deleteMembership(INCORRECT_FLATSHARE_ID, userId0)).isInstanceOf(
                EntityConflictException.class);
    }

    @Test
    public void deleteMembership_WithIncorrectUserId_ReturnsConflict() {
        assertThatThrownBy(() -> membershipService.deleteMembership(FLATSHARE_ID, INCORRECT_USER_ID)).isInstanceOf(
                EntityConflictException.class);
    }
}