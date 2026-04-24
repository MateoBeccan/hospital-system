package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.ContactoEmergencia;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.service.dto.ContactoEmergenciaDTO;
import com.mycompany.hospital.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactoEmergencia} and its DTO {@link ContactoEmergenciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactoEmergenciaMapper extends EntityMapper<ContactoEmergenciaDTO, ContactoEmergencia> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaId")
    ContactoEmergenciaDTO toDto(ContactoEmergencia s);

    @Named("personaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonaDTO toDtoPersonaId(Persona persona);
}
