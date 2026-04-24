package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.service.dto.EspecialidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Especialidad} and its DTO {@link EspecialidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EspecialidadMapper extends EntityMapper<EspecialidadDTO, Especialidad> {}
