package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Enfermero;
import com.mycompany.hospital.repository.EnfermeroRepository;
import com.mycompany.hospital.service.dto.EnfermeroDTO;
import com.mycompany.hospital.service.mapper.EnfermeroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Enfermero}.
 */
@Service
@Transactional
public class EnfermeroService {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermeroService.class);

    private final EnfermeroRepository enfermeroRepository;

    private final EnfermeroMapper enfermeroMapper;

    public EnfermeroService(EnfermeroRepository enfermeroRepository, EnfermeroMapper enfermeroMapper) {
        this.enfermeroRepository = enfermeroRepository;
        this.enfermeroMapper = enfermeroMapper;
    }

    /**
     * Save a enfermero.
     *
     * @param enfermeroDTO the entity to save.
     * @return the persisted entity.
     */
    public EnfermeroDTO save(EnfermeroDTO enfermeroDTO) {
        LOG.debug("Request to save Enfermero : {}", enfermeroDTO);
        Enfermero enfermero = enfermeroMapper.toEntity(enfermeroDTO);
        enfermero = enfermeroRepository.save(enfermero);
        return enfermeroMapper.toDto(enfermero);
    }

    /**
     * Update a enfermero.
     *
     * @param enfermeroDTO the entity to save.
     * @return the persisted entity.
     */
    public EnfermeroDTO update(EnfermeroDTO enfermeroDTO) {
        LOG.debug("Request to update Enfermero : {}", enfermeroDTO);
        Enfermero enfermero = enfermeroMapper.toEntity(enfermeroDTO);
        enfermero = enfermeroRepository.save(enfermero);
        return enfermeroMapper.toDto(enfermero);
    }

    /**
     * Partially update a enfermero.
     *
     * @param enfermeroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EnfermeroDTO> partialUpdate(EnfermeroDTO enfermeroDTO) {
        LOG.debug("Request to partially update Enfermero : {}", enfermeroDTO);

        return enfermeroRepository
            .findById(enfermeroDTO.getId())
            .map(existingEnfermero -> {
                enfermeroMapper.partialUpdate(existingEnfermero, enfermeroDTO);

                return existingEnfermero;
            })
            .map(enfermeroRepository::save)
            .map(enfermeroMapper::toDto);
    }

    /**
     * Get one enfermero by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnfermeroDTO> findOne(Long id) {
        LOG.debug("Request to get Enfermero : {}", id);
        return enfermeroRepository.findById(id).map(enfermeroMapper::toDto);
    }

    /**
     * Delete the enfermero by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Enfermero : {}", id);
        enfermeroRepository.deleteById(id);
    }
}
