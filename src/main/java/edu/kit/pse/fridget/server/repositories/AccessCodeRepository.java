package edu.kit.pse.fridget.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import edu.kit.pse.fridget.server.models.AccessCode;

public interface AccessCodeRepository extends JpaRepository<AccessCode, String> {
    Optional<AccessCode> findByContent(String content);
}
