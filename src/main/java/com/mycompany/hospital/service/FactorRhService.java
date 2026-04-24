package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.repository.FactorRhRepository;
import com.mycompany.hospital.service.dto.FactorRhDTO;
import com.mycompany.hospital.service.mapper.FactorRhMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.FactorRh}.
 */
@Service
@Transactional
public class FactorRhService {

    private static final Logger LOG = LoggerFactory.getLogger(FactorRhService.class);

    private final FactorRhRepository factorRhRepository;

    private final FactorRhMapper factorRhMapper;

    public FactorRhService(FactorRhRepository factorRhRepository, FactorRhMapper factorRhMapper) {
        this.factorRhRepository = factorRhRepository;
        this.factorRhMapper = factorRhMapper;
    }

    /**
     * Save a factorRh.
     *
     * @param factorRhDTO the entity to save.
     * @return the persisted entity.
     */
    public FactorRhDTO save(FactorRhDTO factorRhDTO) {
        LOG.debug("Request to save FactorRh : {}", factorRhDTO);
        FactorRh factorRh = factorRhMapper.toEntity(factorRhDTO);
        factorRh = factorRhRepository.save(factorRh);
        return factorRhMapper.toDto(factorRh);
    }

    /**
     * Update a factorRh.
     *
     * @param factorRhDTO the entity to save.
     * @return the persisted entity.
     */
    public FactorRhDTO update(FactorRhDTO factorRhDTO) {
        LOG.debug("Request to update FactorRh : {}", factorRhDTO);
        FactorRh factorRh = factorRhMapper.toEntity(factorRhDTO);
        factorRh = factorRhRepository.save(factorRh);
        return factorRhMapper.toDto(factorRh);
    }

    /**
     * Partially update a factorRh.
     *
     * @param factorRhDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FactorRhDTO> partialUpdate(FactorRhDTO factorRhDTO) {
        LOG.debug("Request to partially update FactorRh : {}", factorRhDTO);

        return factorRhRepository
            .findById(factorRhDTO.getId())
            .map(existingFactorRh -> {
                factorRhMapper.partialUpdate(existingFactorRh, factorRhDTO);

                return existingFactorRh;
            })
            .map(factorRhRepository::save)
            .map(factorRhMapper::toDto);
    }

    /**
     * Get one factorRh by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FactorRhDTO> findOne(Long id) {
        LOG.debug("Request to get FactorRh : {}", id);
        return factorRhRepository.findById(id).map(factorRhMapper::toDto);
    }

    /**
     * Delete the factorRh by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FactorRh : {}", id);
        factorRhRepository.deleteById(id);
    }
}
