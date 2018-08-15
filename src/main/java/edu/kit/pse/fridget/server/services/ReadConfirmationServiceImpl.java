package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        coolNoteRepository.findById(coolNoteId).orElseThrow(() -> new EntityNotFoundException("Cool Note", coolNoteId));

        return readConfirmationRepository.findByCoolNoteId(coolNoteId).stream()
                .map(readConfirmation -> membershipRepository.findById(readConfirmation.getMembershipId())
                        .orElseThrow(EntityUnprocessableException::new))
                .collect(Collectors.toList());
    }

    @Override
    public ReadConfirmation saveReadConfirmation(ReadConfirmation readConfirmation) {
        String membershipId = readConfirmation.getMembershipId();
        String coolNoteId = readConfirmation.getCoolNoteId();

        membershipRepository.findById(membershipId).orElseThrow(EntityUnprocessableException::new);
        coolNoteRepository.findById(coolNoteId).orElseThrow(EntityUnprocessableException::new);

        return readConfirmationRepository.findByCoolNoteIdAndMembershipId(coolNoteId, membershipId)
                .orElseGet(() -> readConfirmationRepository.save(ReadConfirmation.buildNew(membershipId, coolNoteId)));
    }

    @Override
    public void deleteReadConfirmation(String coolNoteId, String membershipId) {
        coolNoteRepository.findById(coolNoteId).orElseThrow(() -> new EntityNotFoundException("Cool Note", coolNoteId));
        membershipRepository.findById(membershipId).orElseThrow(() -> new EntityNotFoundException("Membership", membershipId));

        readConfirmationRepository.findByCoolNoteIdAndMembershipId(coolNoteId, membershipId)
                .orElseThrow(() -> new EntityConflictException("Read confirmation cannot be deleted, it does not exist."));

        readConfirmationRepository.deleteByCoolNoteIdAndMembershipId(coolNoteId, membershipId);
    }
}
