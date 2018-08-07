package edu.kit.pse.fridget.server.services;

import com.google.firebase.messaging.Notification;
import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.TaggedMember;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.TaggedMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoolNoteServiceImpl implements CoolNoteService {
    private final CoolNoteRepository coolNoteRepository;
    private final MembershipRepository membershipRepository;
    private final DeviceRepository deviceRepository;
    private final TaggedMemberRepository taggedMemberRepository;
    private final FirebaseService firebaseService;

    @Autowired
    public CoolNoteServiceImpl(CoolNoteRepository coolNoteRepository, MembershipRepository membershipRepository,
                               DeviceRepository deviceRepository, TaggedMemberRepository taggedMemberRepository, FirebaseService firebaseService) {
        this.coolNoteRepository = coolNoteRepository;
        this.membershipRepository = membershipRepository;
        this.deviceRepository = deviceRepository;
        this.taggedMemberRepository = taggedMemberRepository;
        this.firebaseService = firebaseService;
    }

    @Override
    public List<CoolNote> getAllCoolNotes(String flatshareId) {
        List<CoolNote> coolNotes = coolNoteRepository.findByFlatshareId(flatshareId)
                .orElseThrow(() -> new EntityNotFoundException("Cool Notes not found."));

        return coolNotes.stream().map(coolNote -> {
            List<TaggedMember> taggedMembers = taggedMemberRepository.findByCoolNoteId(coolNote.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tagged members not found."));

            return taggedMembers.isEmpty() ? CoolNote.buildForFetching(coolNote, new ArrayList<>()) : CoolNote.buildForFetching(coolNote,
                    taggedMembers.stream().map(TaggedMember::getId).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

    @Override
    public CoolNote getCoolNote(String id) {
        CoolNote coolNote = coolNoteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cool Note", id));

        List<TaggedMember> taggedMembers = taggedMemberRepository.findByCoolNoteId(id)
                .orElseThrow(() -> new EntityNotFoundException("Tagged members not found."));

        return taggedMembers.isEmpty() ? CoolNote.buildForFetching(coolNote, new ArrayList<>()) : CoolNote.buildForFetching(coolNote,
                taggedMembers.stream().map(TaggedMember::getId).collect(Collectors.toList()));
    }

    @Override
    public CoolNote saveCoolNote(CoolNote coolNote) {
        Membership creatorMembership = membershipRepository.findById(coolNote.getCreatorMembershipId())
                .orElseThrow(EntityUnprocessableException::new);

        if (coolNoteRepository.findAll()
                .stream()
                .map(CoolNote::getPosition)
                .collect(Collectors.toList())
                .contains(coolNote.getPosition())) {
            throw new EntityConflictException(String.format("Position %s invalid.", coolNote.getPosition()));
        }

        CoolNote newCoolNote = coolNoteRepository.save(coolNote);

        /*        String coolNoteId = newCoolNote.getId();

        Optional<List<TaggedMember>> taggedMembers = taggedMemberRepository.findByCoolNoteId(coolNoteId);
        if (taggedMembers.isPresent() && !taggedMembers.get().isEmpty()) {
            sendMessagesToTaggedMembers(taggedMembers.get(), coolNoteId);
        } else {
            sendMessagesToAll(creatorMembership, coolNoteId);
        }*/

        return newCoolNote;
    }

    @Override
    public void deleteCoolNote(String id) {
        coolNoteRepository.findById(id).orElseThrow(() -> new EntityConflictException("Cool Note", id));

        coolNoteRepository.deleteById(id);
    }

    private void sendMessagesToAll(Membership creatorMembership, String coolNoteId) {
        sendMessages(membershipRepository.findByFlatshareId(creatorMembership.getFlatshareId())
                .get()
                .stream()
                .filter(membership -> !membership.getUserId().equals(creatorMembership.getUserId()))
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
        firebaseService.initializeApp();

        tokens.forEach(token -> firebaseService.sendMessage(token, new Notification("Fridget", "A new Cool Note is created!"), coolNoteId));
    }
}
