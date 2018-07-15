package edu.kit.pse.fridget.server.services;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.repositories.AccessCodeRepository;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

public class AccessCodeServiceTest extends AbstractServiceTest {
    @InjectMocks
    private AccessCodeServiceImpl service;
    @Mock
    private AccessCodeRepository repository;

    @Test
    public void generateAccessCode() {
        service.generateAccessCode(FLATSHARE_ID);

        ArgumentCaptor<AccessCode> accessCodeArgumentCaptor = ArgumentCaptor.forClass(AccessCode.class);
        verify(repository).save(accessCodeArgumentCaptor.capture());

        AccessCode accessCodeToSave = accessCodeArgumentCaptor.getValue();
        assertThat(accessCodeToSave.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(accessCodeToSave.getContent()).matches(Pattern.ACCESS_CODE_PATTERN);
        assertThat(accessCodeToSave.getFlatshareId()).isEqualTo(FLATSHARE_ID);
    }
}