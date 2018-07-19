package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.repositories.AccessCodeRepository;
import edu.kit.pse.fridget.server.repositories.FlatshareRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccessCodeServiceTest extends AbstractServiceTest {
    @InjectMocks
    private AccessCodeServiceImpl service;
    @Mock
    private AccessCodeRepository accessCodeRepository;
    @Mock
    private FlatshareRepository flatshareRepository;

    @Before
    public void setUp() {
        when(flatshareRepository.findById(FLATSHARE_ID)).thenReturn(Optional.of(Flatshare.buildNew("dummy-flatshare")));
        when(flatshareRepository.findById(INCORRECT_FLATSHARE_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void generateAccessCode() {
        service.generateAccessCode(FLATSHARE_ID);

        ArgumentCaptor<AccessCode> accessCodeArgumentCaptor = ArgumentCaptor.forClass(AccessCode.class);
        verify(accessCodeRepository).save(accessCodeArgumentCaptor.capture());

        AccessCode accessCodeToSave = accessCodeArgumentCaptor.getValue();
        assertThat(accessCodeToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(accessCodeToSave.getContent()).matches(Pattern.ACCESS_CODE_PATTERN);
        assertThat(accessCodeToSave.getFlatshareId()).isEqualTo(FLATSHARE_ID);
    }

    @Test
    public void generateAccessCode_WithIncorrectFlatshareId() {
        assertThatThrownBy(() -> service.generateAccessCode(INCORRECT_FLATSHARE_ID)).isInstanceOf(EntityUnprocessableException.class);
    }
}