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

import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserMembershipRepresentation;
import edu.kit.pse.fridget.server.repositories.AccessCodeRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MembershipServiceTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String GOOGLE_USER_ID_0 = "valid-google-id-0";
    private static final String GOOGLE_USER_ID_1 = "valid-google-id-1";
    private static final String GOOGLE_NAME_0 = "John Doe";
    private static final String GOOGLE_NAME_1 = "Jane Doe";
    private static final String MAGNET_COLOR_0 = "0099cc";
    private static final String MAGNET_COLOR_1 = "ffffff";
    private static final String ACCESS_CODE_CONTENT = "abc42";
    @InjectMocks
    private MembershipServiceImpl membershipService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessCodeRepository accessCodeRepository;
    @Mock
    private MagnetColorService magnetColorService;

    private String userId0;
    private String userId1;
    private String membershipId0;
    private String membershipId1;
    private List<Membership> memberships = new ArrayList<>();

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

        memberships.add(membership0);
        memberships.add(membership1);

        when(membershipRepository.findByFlatshareId(FLATSHARE_ID)).thenReturn(memberships);
        when(membershipRepository.findByFlatshareIdAndUserId(FLATSHARE_ID, userId0)).thenReturn(membership0);
        when(userRepository.getOne(userId0)).thenReturn(user0);
        when(userRepository.getOne(userId1)).thenReturn(user1);
        when(accessCodeRepository.findByContent(ACCESS_CODE_CONTENT)).thenReturn(AccessCode.buildNew(ACCESS_CODE_CONTENT, FLATSHARE_ID));
        when(magnetColorService.getRandomColor(FLATSHARE_ID)).thenReturn(MAGNET_COLOR_1);
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
    public void getMember() {
        UserMembershipRepresentation userMembershipRepresentation = membershipService.getMember(FLATSHARE_ID, userId0);

        assertThat(userMembershipRepresentation.getMembershipId()).isEqualTo(membershipId0);
        assertThat(userMembershipRepresentation.getGoogleName()).isEqualTo(GOOGLE_NAME_0);
        assertThat(userMembershipRepresentation.getMagnetColor()).isEqualTo(MAGNET_COLOR_0);
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
}