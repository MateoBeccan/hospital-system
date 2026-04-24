package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TurnoLaboral} and its DTO {@link TurnoLaboralDTO}.
 */
@Mapper(componentModel = "spring")
public interface TurnoLaboralMapper extends EntityMapper<TurnoLaboralDTO, TurnoLaboral> {}
