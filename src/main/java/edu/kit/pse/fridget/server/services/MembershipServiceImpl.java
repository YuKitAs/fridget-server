package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.pse.fridget.server.exceptions.EntityConflictException;
import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserMembershipRepresentation;
import edu.kit.pse.fridget.server.repositories.AccessCodeRepository;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;

@Service
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final AccessCodeRepository accessCodeRepository;
    private final FlatshareRepository flatshareRepository;
    private final MagnetColorService magnetColorService;

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository, UserRepository userRepository,
            AccessCodeRepository accessCodeRepository, FlatshareRepository flatshareRepository, MagnetColorService magnetColorService) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.accessCodeRepository = accessCodeRepository;
        this.flatshareRepository = flatshareRepository;
        this.magnetColorService = magnetColorService;
    }

    @Override
    public List<UserMembershipRepresentation> getAllMembers(String flatshareId) {
        flatshareRepository.findById(flatshareId).orElseThrow(() -> new EntityNotFoundException("Flatshare", flatshareId));

        return membershipRepository.findByFlatshareId(flatshareId).stream()
                .map(membership -> UserMembershipRepresentation.buildFromUserAndMembership(
                        userRepository.findById(membership.getUserId()).orElseThrow(EntityUnprocessableException::new), membership))
                .collect(Collectors.toList());
    }

    @Override
    public UserMembershipRepresentation getMember(String flatshareId, String userId) {
        flatshareRepository.findById(flatshareId).orElseThrow(() -> new EntityNotFoundException("Flatshare", flatshareId));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));

        Membership membership = membershipRepository.findByFlatshareIdAndUserId(flatshareId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found."));

        return UserMembershipRepresentation.buildFromUserAndMembership(user, membership);
    }

    @Override
    public Membership saveMembership(String accessCodeContent, String userId, Membership.Builder membershipBuilder) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));

        AccessCode accessCode = accessCodeRepository.findByContent(accessCodeContent)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Access code \"%s\" not found.", accessCodeContent)));

        String flatshareId = accessCode.getFlatshareId();
        flatshareRepository.findById(flatshareId).orElseThrow(EntityUnprocessableException::new);

        accessCodeRepository.deleteByContent(accessCodeContent);

        if (membershipRepository.findByFlatshareIdAndUserId(flatshareId, userId).isPresent()) {
            throw new EntityConflictException("Membership already exists.");
        }

        if ((membershipRepository.findByFlatshareId(flatshareId).size() == 15)) {
            throw new EntityConflictException("Flatshare already full.");
        }

        return saveMembership(membershipBuilder.setRandomId()
                .setUserId(userId)
                .setFlatshareId(flatshareId)
                .setMagnetColor(magnetColorService.getAvailableRandomColor(flatshareId))
                .build());
    }

    @Override
    public Membership saveMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    @Override
    public void deleteMembership(String flatshareId, String userId) {
        flatshareRepository.findById(flatshareId).orElseThrow(() -> new EntityNotFoundException("Flatshare", flatshareId));
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));

        membershipRepository.delete(membershipRepository.findByFlatshareIdAndUserId(flatshareId, userId)
                .orElseThrow(() -> new EntityConflictException("Membership cannot be deleted, it does not exist.")));

        if (membershipRepository.findByFlatshareId(flatshareId).isEmpty()) {
            flatshareRepository.deleteById(flatshareId);
        }
    }
}
