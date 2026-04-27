package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.CanalSolicitud;
import com.mycompany.hospital.service.dto.CanalSolicitudDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CanalSolicitud} and its DTO {@link CanalSolicitudDTO}.
 */
@Mapper(componentModel = "spring")
public interface CanalSolicitudMapper extends EntityMapper<CanalSolicitudDTO, CanalSolicitud> {}
