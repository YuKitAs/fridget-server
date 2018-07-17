package edu.kit.pse.fridget.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import edu.kit.pse.fridget.server.models.TaggedMember;

public interface TaggedMemberRepository extends JpaRepository<TaggedMember, String> {
    Optional<List<TaggedMember>> findByCoolNoteId(String coolNoteId);
}
