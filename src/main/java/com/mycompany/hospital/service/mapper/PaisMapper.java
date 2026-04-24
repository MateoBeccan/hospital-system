package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Pais;
import com.mycompany.hospital.service.dto.PaisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pais} and its DTO {@link PaisDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaisMapper extends EntityMapper<PaisDTO, Pais> {}
