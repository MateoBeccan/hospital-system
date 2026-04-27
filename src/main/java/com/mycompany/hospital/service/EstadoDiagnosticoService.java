package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.repository.EstadoDiagnosticoRepository;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.EstadoDiagnosticoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.EstadoDiagnostico}.
 */
@Service
@Transactional
public class EstadoDiagnosticoService {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoDiagnosticoService.class);

    private final EstadoDiagnosticoRepository estadoDiagnosticoRepository;

    private final EstadoDiagnosticoMapper estadoDiagnosticoMapper;

    public EstadoDiagnosticoService(
        EstadoDiagnosticoRepository estadoDiagnosticoRepository,
        EstadoDiagnosticoMapper estadoDiagnosticoMapper
    ) {
        this.estadoDiagnosticoRepository = estadoDiagnosticoRepository;
        this.estadoDiagnosticoMapper = estadoDiagnosticoMapper;
    }

    /**
     * Save a estadoDiagnostico.
     *
     * @param estadoDiagnosticoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoDiagnosticoDTO save(EstadoDiagnosticoDTO estadoDiagnosticoDTO) {
        LOG.debug("Request to save EstadoDiagnostico : {}", estadoDiagnosticoDTO);
        EstadoDiagnostico estadoDiagnostico = estadoDiagnosticoMapper.toEntity(estadoDiagnosticoDTO);
        estadoDiagnostico = estadoDiagnosticoRepository.save(estadoDiagnostico);
        return estadoDiagnosticoMapper.toDto(estadoDiagnostico);
    }

    /**
     * Update a estadoDiagnostico.
     *
     * @param estadoDiagnosticoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoDiagnosticoDTO update(EstadoDiagnosticoDTO estadoDiagnosticoDTO) {
        LOG.debug("Request to update EstadoDiagnostico : {}", estadoDiagnosticoDTO);
        EstadoDiagnostico estadoDiagnostico = estadoDiagnosticoMapper.toEntity(estadoDiagnosticoDTO);
        estadoDiagnostico = estadoDiagnosticoRepository.save(estadoDiagnostico);
        return estadoDiagnosticoMapper.toDto(estadoDiagnostico);
    }

    /**
     * Partially update a estadoDiagnostico.
     *
     * @param estadoDiagnosticoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstadoDiagnosticoDTO> partialUpdate(EstadoDiagnosticoDTO estadoDiagnosticoDTO) {
        LOG.debug("Request to partially update EstadoDiagnostico : {}", estadoDiagnosticoDTO);

        return estadoDiagnosticoRepository
            .findById(estadoDiagnosticoDTO.getId())
            .map(existingEstadoDiagnostico -> {
                estadoDiagnosticoMapper.partialUpdate(existingEstadoDiagnostico, estadoDiagnosticoDTO);

                return existingEstadoDiagnostico;
            })
            .map(estadoDiagnosticoRepository::save)
            .map(estadoDiagnosticoMapper::toDto);
    }

    /**
     * Get one estadoDiagnostico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstadoDiagnosticoDTO> findOne(Long id) {
        LOG.debug("Request to get EstadoDiagnostico : {}", id);
        return estadoDiagnosticoRepository.findById(id).map(estadoDiagnosticoMapper::toDto);
    }

    /**
     * Delete the estadoDiagnostico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EstadoDiagnostico : {}", id);
        estadoDiagnosticoRepository.deleteById(id);
    }
}
