package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.GrupoSanguineoAsserts.*;
import static com.mycompany.hospital.domain.GrupoSanguineoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrupoSanguineoMapperTest {

    private GrupoSanguineoMapper grupoSanguineoMapper;

    @BeforeEach
    void setUp() {
        grupoSanguineoMapper = new GrupoSanguineoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGrupoSanguineoSample1();
        var actual = grupoSanguineoMapper.toEntity(grupoSanguineoMapper.toDto(expected));
        assertGrupoSanguineoAllPropertiesEquals(expected, actual);
    }
}
