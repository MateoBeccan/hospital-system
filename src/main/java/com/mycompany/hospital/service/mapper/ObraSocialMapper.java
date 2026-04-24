package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ObraSocial} and its DTO {@link ObraSocialDTO}.
 */
@Mapper(componentModel = "spring")
public interface ObraSocialMapper extends EntityMapper<ObraSocialDTO, ObraSocial> {}
