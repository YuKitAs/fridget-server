package edu.kit.pse.fridget.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.Membership;

public interface MembershipRepository extends JpaRepository<Membership, String> {
    Optional<List<Membership>> findByFlatshareId(String flatshareId);

    Optional<Membership> findByFlatshareIdAndUserId(String flatshareId, String userId);
}
