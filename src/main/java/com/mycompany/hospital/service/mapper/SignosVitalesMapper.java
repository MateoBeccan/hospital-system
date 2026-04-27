package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.SignosVitales;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.dto.SignosVitalesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SignosVitales} and its DTO {@link SignosVitalesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SignosVitalesMapper extends EntityMapper<SignosVitalesDTO, SignosVitales> {
    @Mapping(target = "consulta", source = "consulta", qualifiedByName = "consultaId")
    SignosVitalesDTO toDto(SignosVitales s);

    @Named("consultaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsultaDTO toDtoConsultaId(Consulta consulta);
}
