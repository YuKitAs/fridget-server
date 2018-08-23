package edu.kit.pse.fridget.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import edu.kit.pse.fridget.server.models.Membership;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest {
    static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    static final String INCORRECT_FLATSHARE_ID = "incorrect-flatshare-id";
    static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    static final String INCORRECT_COOL_NOTE_ID = "incorrect-cool-note-id";
    static final String USER_ID_0 = "00000000-0000-0000-0000-000000000000";
    static final String USER_ID_1 = "00000000-0000-0000-0000-000000000001";
    static final String USER_ID_2 = "00000000-0000-0000-0000-000000000002";
    static final String INCORRECT_USER_ID = "incorrect-user-id";
    static final String MAGNET_COLOR_0 = "6ddbff";
    static final String MAGNET_COLOR_1 = "ffffff";
    static final String MAGNET_COLOR_2 = "0054ff";

    static final String FLATSHARE_NOT_FOUND_ERROR_MESSAGE = "Flatshare id=\"incorrect-flatshare-id\" not found.";
    static final String FLATSHARE_FULL_ERROR_MESSAGE = "Flatshare already full.";
    static final String USER_NOT_FOUND_ERROR_MESSAGE = "User id=\"incorrect-user-id\" not found.";
    static final String COOL_NOTE_NOT_FOUND_ERROR_MESSAGE = "Cool Note id=\"incorrect-cool-note-id\" not found.";
    static final String ENTITY_UNPROCESSABLE_ERROR_MESSAGE = "Request contains invalid data that cannot be processed.";

    private static final int MAX_MEMBERSHIP_NUM = 15;

    <T> T getFixture(String filePath, Class<T> objectClass) throws Exception {
        return new ObjectMapper().readValue(new File("src/test/resources/fixtures/" + filePath), objectClass);
    }

    List<Membership> createMembershipList() {
        List<Membership> memberships = new ArrayList<>();

        IntStream.range(0, MAX_MEMBERSHIP_NUM)
                .forEach(i -> memberships.add(new Membership.Builder().setRandomId()
                        .setUserId(UUID.randomUUID().toString())
                        .setFlatshareId(FLATSHARE_ID)
                        .setMagnetColor(MAGNET_COLOR_0)
                        .build()));

        return memberships;
    }
}
