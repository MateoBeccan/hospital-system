package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.repository.EstadoTratamientoRepository;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
import com.mycompany.hospital.service.mapper.EstadoTratamientoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.EstadoTratamiento}.
 */
@Service
@Transactional
public class EstadoTratamientoService {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTratamientoService.class);

    private final EstadoTratamientoRepository estadoTratamientoRepository;

    private final EstadoTratamientoMapper estadoTratamientoMapper;

    public EstadoTratamientoService(
        EstadoTratamientoRepository estadoTratamientoRepository,
        EstadoTratamientoMapper estadoTratamientoMapper
    ) {
        this.estadoTratamientoRepository = estadoTratamientoRepository;
        this.estadoTratamientoMapper = estadoTratamientoMapper;
    }

    /**
     * Save a estadoTratamiento.
     *
     * @param estadoTratamientoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoTratamientoDTO save(EstadoTratamientoDTO estadoTratamientoDTO) {
        LOG.debug("Request to save EstadoTratamiento : {}", estadoTratamientoDTO);
        EstadoTratamiento estadoTratamiento = estadoTratamientoMapper.toEntity(estadoTratamientoDTO);
        estadoTratamiento = estadoTratamientoRepository.save(estadoTratamiento);
        return estadoTratamientoMapper.toDto(estadoTratamiento);
    }

    /**
     * Update a estadoTratamiento.
     *
     * @param estadoTratamientoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoTratamientoDTO update(EstadoTratamientoDTO estadoTratamientoDTO) {
        LOG.debug("Request to update EstadoTratamiento : {}", estadoTratamientoDTO);
        EstadoTratamiento estadoTratamiento = estadoTratamientoMapper.toEntity(estadoTratamientoDTO);
        estadoTratamiento = estadoTratamientoRepository.save(estadoTratamiento);
        return estadoTratamientoMapper.toDto(estadoTratamiento);
    }

    /**
     * Partially update a estadoTratamiento.
     *
     * @param estadoTratamientoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstadoTratamientoDTO> partialUpdate(EstadoTratamientoDTO estadoTratamientoDTO) {
        LOG.debug("Request to partially update EstadoTratamiento : {}", estadoTratamientoDTO);

        return estadoTratamientoRepository
            .findById(estadoTratamientoDTO.getId())
            .map(existingEstadoTratamiento -> {
                estadoTratamientoMapper.partialUpdate(existingEstadoTratamiento, estadoTratamientoDTO);

                return existingEstadoTratamiento;
            })
            .map(estadoTratamientoRepository::save)
            .map(estadoTratamientoMapper::toDto);
    }

    /**
     * Get one estadoTratamiento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstadoTratamientoDTO> findOne(Long id) {
        LOG.debug("Request to get EstadoTratamiento : {}", id);
        return estadoTratamientoRepository.findById(id).map(estadoTratamientoMapper::toDto);
    }

    /**
     * Delete the estadoTratamiento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EstadoTratamiento : {}", id);
        estadoTratamientoRepository.deleteById(id);
    }
}
