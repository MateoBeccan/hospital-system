package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Ciudad;
import com.mycompany.hospital.domain.Provincia;
import com.mycompany.hospital.service.dto.CiudadDTO;
import com.mycompany.hospital.service.dto.ProvinciaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ciudad} and its DTO {@link CiudadDTO}.
 */
@Mapper(componentModel = "spring")
public interface CiudadMapper extends EntityMapper<CiudadDTO, Ciudad> {
    @Mapping(target = "provincia", source = "provincia", qualifiedByName = "provinciaId")
    CiudadDTO toDto(Ciudad s);

    @Named("provinciaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinciaDTO toDtoProvinciaId(Provincia provincia);
}
