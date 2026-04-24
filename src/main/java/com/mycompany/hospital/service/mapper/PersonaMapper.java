package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Ciudad;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.domain.Sexo;
import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.service.dto.CiudadDTO;
import com.mycompany.hospital.service.dto.PersonaDTO;
import com.mycompany.hospital.service.dto.SexoDTO;
import com.mycompany.hospital.service.dto.TipoDocumentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {
    @Mapping(target = "tipoDocumento", source = "tipoDocumento", qualifiedByName = "tipoDocumentoId")
    @Mapping(target = "sexo", source = "sexo", qualifiedByName = "sexoId")
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadId")
    PersonaDTO toDto(Persona s);

    @Named("tipoDocumentoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoDocumentoDTO toDtoTipoDocumentoId(TipoDocumento tipoDocumento);

    @Named("sexoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SexoDTO toDtoSexoId(Sexo sexo);

    @Named("ciudadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CiudadDTO toDtoCiudadId(Ciudad ciudad);
}
