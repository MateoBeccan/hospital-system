package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.repository.TurnoLaboralRepository;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
import com.mycompany.hospital.service.mapper.TurnoLaboralMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.TurnoLaboral}.
 */
@Service
@Transactional
public class TurnoLaboralService {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoLaboralService.class);

    private final TurnoLaboralRepository turnoLaboralRepository;

    private final TurnoLaboralMapper turnoLaboralMapper;

    public TurnoLaboralService(TurnoLaboralRepository turnoLaboralRepository, TurnoLaboralMapper turnoLaboralMapper) {
        this.turnoLaboralRepository = turnoLaboralRepository;
        this.turnoLaboralMapper = turnoLaboralMapper;
    }

    /**
     * Save a turnoLaboral.
     *
     * @param turnoLaboralDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoLaboralDTO save(TurnoLaboralDTO turnoLaboralDTO) {
        LOG.debug("Request to save TurnoLaboral : {}", turnoLaboralDTO);
        TurnoLaboral turnoLaboral = turnoLaboralMapper.toEntity(turnoLaboralDTO);
        turnoLaboral = turnoLaboralRepository.save(turnoLaboral);
        return turnoLaboralMapper.toDto(turnoLaboral);
    }

    /**
     * Update a turnoLaboral.
     *
     * @param turnoLaboralDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoLaboralDTO update(TurnoLaboralDTO turnoLaboralDTO) {
        LOG.debug("Request to update TurnoLaboral : {}", turnoLaboralDTO);
        TurnoLaboral turnoLaboral = turnoLaboralMapper.toEntity(turnoLaboralDTO);
        turnoLaboral = turnoLaboralRepository.save(turnoLaboral);
        return turnoLaboralMapper.toDto(turnoLaboral);
    }

    /**
     * Partially update a turnoLaboral.
     *
     * @param turnoLaboralDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TurnoLaboralDTO> partialUpdate(TurnoLaboralDTO turnoLaboralDTO) {
        LOG.debug("Request to partially update TurnoLaboral : {}", turnoLaboralDTO);

        return turnoLaboralRepository
            .findById(turnoLaboralDTO.getId())
            .map(existingTurnoLaboral -> {
                turnoLaboralMapper.partialUpdate(existingTurnoLaboral, turnoLaboralDTO);

                return existingTurnoLaboral;
            })
            .map(turnoLaboralRepository::save)
            .map(turnoLaboralMapper::toDto);
    }

    /**
     * Get one turnoLaboral by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurnoLaboralDTO> findOne(Long id) {
        LOG.debug("Request to get TurnoLaboral : {}", id);
        return turnoLaboralRepository.findById(id).map(turnoLaboralMapper::toDto);
    }

    /**
     * Delete the turnoLaboral by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TurnoLaboral : {}", id);
        turnoLaboralRepository.deleteById(id);
    }
}
