package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.models.TaggedMember;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.TaggedMemberRepository;

@Service
public class CoolNoteServiceImpl implements CoolNoteService {
    private final CoolNoteRepository coolNoteRepository;
    private final MembershipRepository membershipRepository;
    private final TaggedMemberRepository taggedMemberRepository;

    @Autowired
    public CoolNoteServiceImpl(CoolNoteRepository coolNoteRepository, MembershipRepository membershipRepository,
            TaggedMemberRepository taggedMemberRepository) {
        this.coolNoteRepository = coolNoteRepository;
        this.membershipRepository = membershipRepository;
        this.taggedMemberRepository = taggedMemberRepository;
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

        // theoretically all the members except the creator will be tagged
        List<TaggedMember> taggedMembers = taggedMemberRepository.findByCoolNoteId(id)
                .orElseThrow(() -> new EntityNotFoundException("Tagged members not found."));

        return taggedMembers.isEmpty() ? CoolNote.buildForFetching(coolNote, new ArrayList<>()) : CoolNote.buildForFetching(coolNote,
                taggedMembers.stream().map(TaggedMember::getId).collect(Collectors.toList()));
    }

    @Override
    public CoolNote saveCoolNote(CoolNote coolNote) {
        membershipRepository.findById(coolNote.getCreatorMembershipId()).orElseThrow(EntityUnprocessableException::new);

        if (coolNoteRepository.findAll()
                .stream()
                .map(CoolNote::getPosition)
                .collect(Collectors.toList())
                .contains(coolNote.getPosition())) {
            throw new EntityConflictException(String.format("Position %s invalid.", coolNote.getPosition()));
        }

        return coolNoteRepository.save(coolNote);
    }

    @Override
    public void deleteCoolNote(String id) {
        coolNoteRepository.findById(id).orElseThrow(() -> new EntityConflictException("Cool Note", id));

        coolNoteRepository.deleteById(id);
    }
}
