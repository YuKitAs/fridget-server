package edu.kit.pse.fridget.server.utilities;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternTest {
    @Test
    public void uuidPattern() {
        assertThat("d5113666-a0d8-11e8-98d0-529269fb1459").matches(Pattern.UUID_PATTERN);
        assertThat("10866270-a0d9-11e8-b568-0800200c9a66").matches(Pattern.UUID_PATTERN);
        assertThat("b58a0fba-718e-4fe2-9d10-aa96fbf087ef").matches(Pattern.UUID_PATTERN);
    }

    @Test
    public void accessCodePattern() {
        assertThat("34bha").matches(Pattern.ACCESS_CODE_PATTERN);
        assertThat("va62W").matches(Pattern.ACCESS_CODE_PATTERN);
        assertThat("YYI2d").matches(Pattern.ACCESS_CODE_PATTERN);
    }

    @Test
    public void hexColorCodePattern() {
        assertThat("75FFCA").matches(Pattern.HEX_COLOR_CODE_PATTERN);
        assertThat("63536C").matches(Pattern.HEX_COLOR_CODE_PATTERN);
        assertThat("C39015").matches(Pattern.HEX_COLOR_CODE_PATTERN);
    }
}