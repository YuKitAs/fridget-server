package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.IntStream;

import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.FrozenNote;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;

@Service
public class FlatshareServiceImpl implements FlatshareService {
    private final FlatshareRepository flatshareRepository;
    private final UserRepository userRepository;
    private final MembershipService membershipService;
    private final FrozenNoteService frozenNoteService;
    private final MagnetColorService magnetColorService;

    @Autowired
    public FlatshareServiceImpl(FlatshareRepository flatshareRepository, UserRepository userRepository, MembershipService membershipService,
            FrozenNoteService frozenNoteService, MagnetColorService magnetColorService) {
        this.flatshareRepository = flatshareRepository;
        this.userRepository = userRepository;
        this.membershipService = membershipService;
        this.frozenNoteService = frozenNoteService;
        this.magnetColorService = magnetColorService;
    }

    @Override
    public Flatshare getFlatshare(String id) {
        return flatshareRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flatshare", id));
    }

    @Override
    public Flatshare saveFlatshare(Flatshare flatshare, String userId) {
        userRepository.findById(userId).orElseThrow(EntityUnprocessableException::new);

        Flatshare createdFlatshare = flatshareRepository.save(flatshare);

        membershipService.saveMembership(new Membership.Builder().setRandomId()
                .setMagnetColor(magnetColorService.getRandomColor())
                .setFlatshareId(createdFlatshare.getId())
                .setUserId(userId)
                .build());

        IntStream.range(0, 3)
                .forEach(i -> frozenNoteService.saveFrozenNote(
                        new FrozenNote(UUID.randomUUID().toString(), createdFlatshare.getId(), "", "", i)));

        return createdFlatshare;
    }
}
