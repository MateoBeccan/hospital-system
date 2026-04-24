package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.repository.EstadoLaboralRepository;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
import com.mycompany.hospital.service.mapper.EstadoLaboralMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.EstadoLaboral}.
 */
@Service
@Transactional
public class EstadoLaboralService {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoLaboralService.class);

    private final EstadoLaboralRepository estadoLaboralRepository;

    private final EstadoLaboralMapper estadoLaboralMapper;

    public EstadoLaboralService(EstadoLaboralRepository estadoLaboralRepository, EstadoLaboralMapper estadoLaboralMapper) {
        this.estadoLaboralRepository = estadoLaboralRepository;
        this.estadoLaboralMapper = estadoLaboralMapper;
    }

    /**
     * Save a estadoLaboral.
     *
     * @param estadoLaboralDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoLaboralDTO save(EstadoLaboralDTO estadoLaboralDTO) {
        LOG.debug("Request to save EstadoLaboral : {}", estadoLaboralDTO);
        EstadoLaboral estadoLaboral = estadoLaboralMapper.toEntity(estadoLaboralDTO);
        estadoLaboral = estadoLaboralRepository.save(estadoLaboral);
        return estadoLaboralMapper.toDto(estadoLaboral);
    }

    /**
     * Update a estadoLaboral.
     *
     * @param estadoLaboralDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoLaboralDTO update(EstadoLaboralDTO estadoLaboralDTO) {
        LOG.debug("Request to update EstadoLaboral : {}", estadoLaboralDTO);
        EstadoLaboral estadoLaboral = estadoLaboralMapper.toEntity(estadoLaboralDTO);
        estadoLaboral = estadoLaboralRepository.save(estadoLaboral);
        return estadoLaboralMapper.toDto(estadoLaboral);
    }

    /**
     * Partially update a estadoLaboral.
     *
     * @param estadoLaboralDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstadoLaboralDTO> partialUpdate(EstadoLaboralDTO estadoLaboralDTO) {
        LOG.debug("Request to partially update EstadoLaboral : {}", estadoLaboralDTO);

        return estadoLaboralRepository
            .findById(estadoLaboralDTO.getId())
            .map(existingEstadoLaboral -> {
                estadoLaboralMapper.partialUpdate(existingEstadoLaboral, estadoLaboralDTO);

                return existingEstadoLaboral;
            })
            .map(estadoLaboralRepository::save)
            .map(estadoLaboralMapper::toDto);
    }

    /**
     * Get one estadoLaboral by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstadoLaboralDTO> findOne(Long id) {
        LOG.debug("Request to get EstadoLaboral : {}", id);
        return estadoLaboralRepository.findById(id).map(estadoLaboralMapper::toDto);
    }

    /**
     * Delete the estadoLaboral by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EstadoLaboral : {}", id);
        estadoLaboralRepository.deleteById(id);
    }
}
