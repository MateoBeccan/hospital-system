package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.repository.TipoDocumentoRepository;
import com.mycompany.hospital.service.dto.TipoDocumentoDTO;
import com.mycompany.hospital.service.mapper.TipoDocumentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.TipoDocumento}.
 */
@Service
@Transactional
public class TipoDocumentoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDocumentoService.class);

    private final TipoDocumentoRepository tipoDocumentoRepository;

    private final TipoDocumentoMapper tipoDocumentoMapper;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository, TipoDocumentoMapper tipoDocumentoMapper) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.tipoDocumentoMapper = tipoDocumentoMapper;
    }

    /**
     * Save a tipoDocumento.
     *
     * @param tipoDocumentoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDocumentoDTO save(TipoDocumentoDTO tipoDocumentoDTO) {
        LOG.debug("Request to save TipoDocumento : {}", tipoDocumentoDTO);
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(tipoDocumentoDTO);
        tipoDocumento = tipoDocumentoRepository.save(tipoDocumento);
        return tipoDocumentoMapper.toDto(tipoDocumento);
    }

    /**
     * Update a tipoDocumento.
     *
     * @param tipoDocumentoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDocumentoDTO update(TipoDocumentoDTO tipoDocumentoDTO) {
        LOG.debug("Request to update TipoDocumento : {}", tipoDocumentoDTO);
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(tipoDocumentoDTO);
        tipoDocumento = tipoDocumentoRepository.save(tipoDocumento);
        return tipoDocumentoMapper.toDto(tipoDocumento);
    }

    /**
     * Partially update a tipoDocumento.
     *
     * @param tipoDocumentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoDocumentoDTO> partialUpdate(TipoDocumentoDTO tipoDocumentoDTO) {
        LOG.debug("Request to partially update TipoDocumento : {}", tipoDocumentoDTO);

        return tipoDocumentoRepository
            .findById(tipoDocumentoDTO.getId())
            .map(existingTipoDocumento -> {
                tipoDocumentoMapper.partialUpdate(existingTipoDocumento, tipoDocumentoDTO);

                return existingTipoDocumento;
            })
            .map(tipoDocumentoRepository::save)
            .map(tipoDocumentoMapper::toDto);
    }

    /**
     * Get one tipoDocumento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoDocumentoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoDocumento : {}", id);
        return tipoDocumentoRepository.findById(id).map(tipoDocumentoMapper::toDto);
    }

    /**
     * Delete the tipoDocumento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoDocumento : {}", id);
        tipoDocumentoRepository.deleteById(id);
    }
}
