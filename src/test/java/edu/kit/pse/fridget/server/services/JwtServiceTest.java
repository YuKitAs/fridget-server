package edu.kit.pse.fridget.server.services;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {
    @Test
    public void encodeAndDecode_ReturnsCorrectUserId() {
        String userId = "00000000-0000-0000-0000-000000000000";
        String token = JwtService.encode(userId);

        assertThat(JwtService.decode(token)).isEqualTo(userId);
    }
}