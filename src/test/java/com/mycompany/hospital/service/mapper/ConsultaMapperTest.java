package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.ConsultaAsserts.*;
import static com.mycompany.hospital.domain.ConsultaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultaMapperTest {

    private ConsultaMapper consultaMapper;

    @BeforeEach
    void setUp() {
        consultaMapper = new ConsultaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsultaSample1();
        var actual = consultaMapper.toEntity(consultaMapper.toDto(expected));
        assertConsultaAllPropertiesEquals(expected, actual);
    }
}
