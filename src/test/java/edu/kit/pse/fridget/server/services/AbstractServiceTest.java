package edu.kit.pse.fridget.server.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

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
    static final String USER_NOT_FOUND_ERROR_MESSAGE = "User id=\"incorrect-user-id\" not found.";
    static final String COOL_NOTE_NOT_FOUND_ERROR_MESSAGE = "Cool Note id=\"incorrect-cool-note-id\" not found.";
    static final String ENTITY_UNPROCESSABLE_ERROR_MESSAGE = "Request contains invalid data that cannot be processed.";

    <T> T getFixture(String filePath, Class<T> objectClass) throws Exception {
        return new ObjectMapper().readValue(new File("src/test/resources/fixtures/" + filePath), objectClass);
    }
}
