package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.SignosVitalesAsserts.*;
import static com.mycompany.hospital.domain.SignosVitalesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignosVitalesMapperTest {

    private SignosVitalesMapper signosVitalesMapper;

    @BeforeEach
    void setUp() {
        signosVitalesMapper = new SignosVitalesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSignosVitalesSample1();
        var actual = signosVitalesMapper.toEntity(signosVitalesMapper.toDto(expected));
        assertSignosVitalesAllPropertiesEquals(expected, actual);
    }
}
