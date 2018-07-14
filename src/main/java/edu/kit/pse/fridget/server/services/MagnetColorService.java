package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;

@Service
public class MagnetColorService {
    private static final String[] COLORS = {"6ddbff", "0054ff", "66666b", "cc0cf9", "cc0099", "7b1100", "ff0000", "ff9900", "f9f22a",
            "0eac0e", "006600", "009999", "ffcccc", "ffffff", "bdbdbd"};
    private final MembershipRepository membershipRepository;

    @Autowired
    public MagnetColorService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    String getRandomColor() {
        return COLORS[new Random().nextInt(15)];
    }

    String getAvailableRandomColor(String flatshareId) {
        List<String> magnetColors = membershipRepository.findByFlatshareId(flatshareId)
                .stream()
                .map(Membership::getMagnetColor)
                .collect(Collectors.toList());

        List<String> availableColors = Arrays.stream(COLORS)
                .collect(Collectors.toList())
                .stream()
                .filter(color -> !magnetColors.contains(color))
                .collect(Collectors.toList());

        return availableColors.get(new Random().nextInt(availableColors.size()));
    }
}
