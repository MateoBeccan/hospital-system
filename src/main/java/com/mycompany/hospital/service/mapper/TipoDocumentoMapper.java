package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.service.dto.TipoDocumentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoDocumento} and its DTO {@link TipoDocumentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoDocumentoMapper extends EntityMapper<TipoDocumentoDTO, TipoDocumento> {}
