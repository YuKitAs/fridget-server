package edu.kit.pse.fridget.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import edu.kit.pse.fridget.server.configurations.AuthenticationServiceConfiguration;
import edu.kit.pse.fridget.server.configurations.FirebaseServiceConfiguration;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {AuthenticationServiceConfiguration.class, FirebaseServiceConfiguration.class})
@SqlGroup({ //
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:beforeTestRun.sql"}), //
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql") //
})
public abstract class AbstractControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    static final String ENTITY_UNPROCESSABLE_ERROR_MESSAGE = "Request contains invalid data that cannot be processed.";

    TestRestTemplate getTestRestTemplate() {
        return testRestTemplate;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    <T> T getFixture(String filePath, Class<T> objectClass) throws Exception {
        return new ObjectMapper().readValue(new File("src/test/resources/fixtures/" + filePath), objectClass);
    }
}