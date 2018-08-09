package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.FrozenNote;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.FrozenNoteRepository;

@Service
public class FrozenNoteServiceImpl implements FrozenNoteService {
    private final FrozenNoteRepository frozenNoteRepository;
    private final FlatshareRepository flatshareRepository;

    @Autowired
    public FrozenNoteServiceImpl(FrozenNoteRepository frozenNoteRepository, FlatshareRepository flatshareRepository) {
        this.frozenNoteRepository = frozenNoteRepository;
        this.flatshareRepository = flatshareRepository;
    }

    @Override
    public List<FrozenNote> getAllFrozenNotes(String flatshareId) {
        return frozenNoteRepository.findByFlatshareId(flatshareId)
                .orElseThrow(() -> new EntityNotFoundException("Frozen Notes not found."));
    }

    @Override
    public FrozenNote getFrozenNote(String id) {
        return frozenNoteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Frozen Note", id));
    }

    @Override
    public FrozenNote updateFrozenNote(String id, FrozenNote frozenNote) {
        if (!id.equals(frozenNote.getId())) {
            throw new EntityUnprocessableException();
        }

        flatshareRepository.findById(frozenNote.getFlatshareId()).orElseThrow(EntityUnprocessableException::new);

        if (frozenNoteRepository.findById(id).get().getPosition() != frozenNote.getPosition()) {
            throw new EntityConflictException(String.format("Position %s invalid.", frozenNote.getPosition()));
        }

        return frozenNoteRepository.save(frozenNote);
    }

    @Override
    public FrozenNote saveFrozenNote(FrozenNote frozenNote) {
        return frozenNoteRepository.save(frozenNote);
    }
}