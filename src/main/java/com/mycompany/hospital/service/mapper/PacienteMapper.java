package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.service.dto.FactorRhDTO;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaId")
    @Mapping(target = "obraSocial", source = "obraSocial", qualifiedByName = "obraSocialId")
    @Mapping(target = "grupoSanguineo", source = "grupoSanguineo", qualifiedByName = "grupoSanguineoId")
    @Mapping(target = "factorRh", source = "factorRh", qualifiedByName = "factorRhId")
    PacienteDTO toDto(Paciente s);

    @Named("personaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonaDTO toDtoPersonaId(Persona persona);

    @Named("obraSocialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObraSocialDTO toDtoObraSocialId(ObraSocial obraSocial);

    @Named("grupoSanguineoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GrupoSanguineoDTO toDtoGrupoSanguineoId(GrupoSanguineo grupoSanguineo);

    @Named("factorRhId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactorRhDTO toDtoFactorRhId(FactorRh factorRh);
}
