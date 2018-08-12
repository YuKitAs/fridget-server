package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;
import edu.kit.pse.fridget.server.repositories.CoolNoteRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.ReadConfirmationRepository;

@Service
public class ReadConfirmationServiceImpl implements ReadConfirmationService {
    private final ReadConfirmationRepository readConfirmationRepository;
    private final MembershipRepository membershipRepository;
    private final CoolNoteRepository coolNoteRepository;

    @Autowired
    public ReadConfirmationServiceImpl(ReadConfirmationRepository readConfirmationRepository, MembershipRepository membershipRepository,
            CoolNoteRepository coolNoteRepository) {
        this.readConfirmationRepository = readConfirmationRepository;
        this.membershipRepository = membershipRepository;
        this.coolNoteRepository = coolNoteRepository;
    }

    @Override
    public List<Membership> getAllMemberships(String coolNoteId) {
        List<ReadConfirmation> readConfirmations = readConfirmationRepository.findByCoolNoteId(coolNoteId)
                .orElseThrow(() -> new EntityNotFoundException("Read confirmations not found."));

        return readConfirmations.isEmpty() ? Collections.emptyList() : readConfirmations.stream()
                .map(readConfirmation -> membershipRepository.findById(readConfirmation.getMembershipId())
                        .orElseThrow(() -> new EntityNotFoundException("Membership not found.")))
                .collect(Collectors.toList());
    }

    @Override
    public ReadConfirmation saveReadConfirmation(ReadConfirmation readConfirmation) {
        String membershipId = readConfirmation.getMembershipId();
        String coolNoteId = readConfirmation.getCoolNoteId();

        membershipRepository.findById(membershipId).orElseThrow(EntityUnprocessableException::new);
        coolNoteRepository.findById(coolNoteId).orElseThrow(EntityUnprocessableException::new);

        Optional<ReadConfirmation> newReadConfirmation = readConfirmationRepository.findByCoolNoteIdAndMembershipId(coolNoteId,
                membershipId);

        return newReadConfirmation.orElseGet(() -> readConfirmationRepository.save(ReadConfirmation.buildNew(membershipId, coolNoteId)));
    }

    @Override
    public void deleteReadConfirmation(String coolNoteId, String membershipId) {
        readConfirmationRepository.findByCoolNoteIdAndMembershipId(coolNoteId, membershipId)
                .orElseThrow(() -> new EntityConflictException("Read confirmation cannot be deleted, it does not exist."));

        readConfirmationRepository.deleteByCoolNoteIdAndMembershipId(coolNoteId, membershipId);
    }
}
