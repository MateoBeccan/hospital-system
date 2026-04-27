package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EstadoTratamiento} and its DTO {@link EstadoTratamientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadoTratamientoMapper extends EntityMapper<EstadoTratamientoDTO, EstadoTratamiento> {}
