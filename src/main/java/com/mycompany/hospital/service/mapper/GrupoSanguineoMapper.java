package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GrupoSanguineo} and its DTO {@link GrupoSanguineoDTO}.
 */
@Mapper(componentModel = "spring")
public interface GrupoSanguineoMapper extends EntityMapper<GrupoSanguineoDTO, GrupoSanguineo> {}
