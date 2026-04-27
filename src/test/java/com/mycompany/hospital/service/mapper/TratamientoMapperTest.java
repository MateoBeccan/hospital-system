package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.TratamientoAsserts.*;
import static com.mycompany.hospital.domain.TratamientoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TratamientoMapperTest {

    private TratamientoMapper tratamientoMapper;

    @BeforeEach
    void setUp() {
        tratamientoMapper = new TratamientoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTratamientoSample1();
        var actual = tratamientoMapper.toEntity(tratamientoMapper.toDto(expected));
        assertTratamientoAllPropertiesEquals(expected, actual);
    }
}
