package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Sexo;
import com.mycompany.hospital.service.dto.SexoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sexo} and its DTO {@link SexoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SexoMapper extends EntityMapper<SexoDTO, Sexo> {}
