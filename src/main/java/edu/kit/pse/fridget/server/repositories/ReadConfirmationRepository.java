package edu.kit.pse.fridget.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.ReadConfirmation;

public interface ReadConfirmationRepository extends JpaRepository<ReadConfirmation, String> {
    Optional<List<ReadConfirmation>> findByCoolNoteId(String id);

    Optional<ReadConfirmation> findByCoolNoteIdAndMembershipId(String coolNoteId, String membershipId);

    void deleteByCoolNoteIdAndMembershipId(String coolNoteId, String membershipId);
}
