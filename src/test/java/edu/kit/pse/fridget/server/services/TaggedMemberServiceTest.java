package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.TaggedMember;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.TaggedMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TaggedMemberServiceTest extends AbstractServiceTest {
    @InjectMocks
    private TaggedMemberServiceImpl service;
    @Mock
    private TaggedMemberRepository taggedMemberRepository;
    @Mock
    private MembershipRepository membershipRepository;

    private Membership membership0;
    private Membership membership1;

    @Before
    public void setUp() throws Exception {
        membership0 = getFixture("membership0.json", Membership.class);
        membership1 = getFixture("membership1.json", Membership.class);

        String membershipId0 = membership0.getId();
        String membershipId1 = membership1.getId();

        List<TaggedMember> taggedMembers = new ArrayList<>();
        taggedMembers.add(TaggedMember.buildNew(membershipId0, COOL_NOTE_ID));
        taggedMembers.add(TaggedMember.buildNew(membershipId1, COOL_NOTE_ID));

        when(taggedMemberRepository.findByCoolNoteId(COOL_NOTE_ID)).thenReturn(Optional.of(taggedMembers));
        when(membershipRepository.findById(membershipId0)).thenReturn(Optional.of(membership0));
        when(membershipRepository.findById(membershipId1)).thenReturn(Optional.of(membership1));
    }

    @Test
    public void getAllTaggedMembers() {
        List<Membership> memberships = service.getAllTaggedMembers(COOL_NOTE_ID);

        assertThat(memberships.size()).isEqualTo(2);
        assertThat(memberships.get(0)).isEqualTo(membership0);
        assertThat(memberships.get(1)).isEqualTo(membership1);
    }
}