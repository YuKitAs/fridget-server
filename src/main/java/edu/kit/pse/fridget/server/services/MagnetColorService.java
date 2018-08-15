package edu.kit.pse.fridget.server.services;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.repositories.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MagnetColorService {
    private static final String[] COLORS = {"6ddbff", "0054ff", "66666b", "cc0cf9", "cc0099", "7b1100", "ff0000", "ff9900", "f9f22a", "0eac0e", "006600", "009999", "ffcccc", "75ffca", "bdbdbd"};
    private final MembershipRepository membershipRepository;
    private List<String> availableColors = new ArrayList<>();

    @Autowired
    public MagnetColorService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    String getRandomColor() {
        return COLORS[new Random().nextInt(15)];
    }

    String getAvailableRandomColor(String flatshareId) {
        membershipRepository.findByFlatshareId(flatshareId).ifPresent(memberships -> {
            List<String> magnetColors = memberships.stream().map(Membership::getMagnetColor).collect(Collectors.toList());

            setAvailableColors(Arrays.stream(COLORS)
                    .collect(Collectors.toList())
                    .stream()
                    .filter(color -> !magnetColors.contains(color))
                    .collect(Collectors.toList()));
        });

        return availableColors.isEmpty() ? getRandomColor() : availableColors.get(new Random().nextInt(availableColors.size()));
    }

    private void setAvailableColors(List<String> availableColors) {
        this.availableColors = availableColors;
    }
}
