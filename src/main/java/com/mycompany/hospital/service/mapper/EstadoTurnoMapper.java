package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EstadoTurno} and its DTO {@link EstadoTurnoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadoTurnoMapper extends EntityMapper<EstadoTurnoDTO, EstadoTurno> {}
