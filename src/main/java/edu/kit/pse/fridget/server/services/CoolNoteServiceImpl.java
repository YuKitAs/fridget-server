package edu.kit.pse.fridget.server.services;

import com.google.firebase.messaging.Notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.TaggedMember;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.TaggedMemberRepository;

@Service
public class CoolNoteServiceImpl implements CoolNoteService {
    private static final Log LOG = LogFactory.getLog(CoolNoteServiceImpl.class);

    private final CoolNoteRepository coolNoteRepository;
    private final MembershipRepository membershipRepository;
    private final DeviceRepository deviceRepository;
    private final TaggedMemberRepository taggedMemberRepository;
    private final FlatshareRepository flatshareRepository;
    private final FirebaseService firebaseService;

    @Autowired
    public CoolNoteServiceImpl(CoolNoteRepository coolNoteRepository, MembershipRepository membershipRepository,
            DeviceRepository deviceRepository, TaggedMemberRepository taggedMemberRepository, FlatshareRepository flatshareRepository,
            FirebaseService firebaseService) {
        this.coolNoteRepository = coolNoteRepository;
        this.membershipRepository = membershipRepository;
        this.deviceRepository = deviceRepository;
        this.taggedMemberRepository = taggedMemberRepository;
        this.flatshareRepository = flatshareRepository;
        this.firebaseService = firebaseService;
    }

    @Override
    public List<CoolNote> getAllCoolNotes(String flatshareId) {
        flatshareRepository.findById(flatshareId).orElseThrow(() -> new EntityNotFoundException("Flatshare", flatshareId));

        return coolNoteRepository.findByFlatshareId(flatshareId)
                .stream()
                .map(coolNote -> CoolNote.buildForFetching(coolNote, taggedMemberRepository.findByCoolNoteId(coolNote.getId())
                        .stream()
                        .map(TaggedMember::getId)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public CoolNote getCoolNote(String id) {
        return CoolNote.buildForFetching(coolNoteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cool Note", id)),
                taggedMemberRepository.findByCoolNoteId(id).stream().map(TaggedMember::getId).collect(Collectors.toList()));
    }

    @Override
    public CoolNote saveCoolNote(CoolNote coolNote) {
        Membership creatorMembership = membershipRepository.findById(coolNote.getCreatorMembershipId())
                .orElseThrow(EntityUnprocessableException::new);

        if (coolNoteRepository.findByFlatshareId(creatorMembership.getFlatshareId())
                .stream()
                .map(CoolNote::getPosition)
                .collect(Collectors.toList())
                .contains(coolNote.getPosition())) {
            throw new EntityConflictException(String.format("Position %s invalid.", coolNote.getPosition()));
        }

        CoolNote newCoolNote = coolNoteRepository.save(coolNote);

        String coolNoteId = newCoolNote.getId();

        List<TaggedMember> taggedMembers = taggedMemberRepository.findByCoolNoteId(coolNoteId);
        if (!taggedMembers.isEmpty()) {
            sendMessagesToTaggedMembers(taggedMembers, coolNoteId);
        } else {
            sendMessagesToAll(creatorMembership, coolNoteId);
        }

        return newCoolNote;
    }

    @Override
    public void deleteCoolNote(String id) {
        coolNoteRepository.findById(id).orElseThrow(() -> new EntityConflictException("Cool Note", id));

        coolNoteRepository.deleteById(id);
    }

    private void sendMessagesToAll(Membership creatorMembership, String coolNoteId) {
        LOG.info("Sending push notification to all roommates of (user ID) " + creatorMembership.getUserId());

        sendMessages(membershipRepository.findByFlatshareId(creatorMembership.getFlatshareId())
                .stream()
                .filter(membership -> !membership.getUserId().equals(creatorMembership.getUserId()))
                .peek(membership -> LOG.info("Push notification will be sent to (user ID)" + membership.getUserId()))
                .map(Membership::getUserId)
                .map(userId -> deviceRepository.findByUserId(userId).get().getInstanceIdToken())
                .collect(Collectors.toList()), coolNoteId);
    }

    private void sendMessagesToTaggedMembers(List<TaggedMember> taggedMembers, String coolNoteId) {
        sendMessages(taggedMembers.stream()
                .map(taggedMember -> membershipRepository.findById(taggedMember.getMembershipId()).get().getUserId())
                .map(userId -> deviceRepository.findByUserId(userId).get().getInstanceIdToken())
                .collect(Collectors.toList()), coolNoteId);
    }

    private void sendMessages(List<String> tokens, String coolNoteId) {
        if (tokens.isEmpty()) {
            return;
        }

        this.firebaseService.initializeApp();

        tokens.forEach(token -> sendMessageToOne(token, coolNoteId));
    }

    private void sendMessageToOne(String token, String coolNoteId) {
        LOG.info("Sending push notification with token " + token);
        firebaseService.sendMessage(token, new Notification("Fridget", "A new Cool Note is created!"), coolNoteId);
    }
}
