package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.repository.EstadoTurnoRepository;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
import com.mycompany.hospital.service.mapper.EstadoTurnoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.EstadoTurno}.
 */
@Service
@Transactional
public class EstadoTurnoService {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTurnoService.class);

    private final EstadoTurnoRepository estadoTurnoRepository;

    private final EstadoTurnoMapper estadoTurnoMapper;

    public EstadoTurnoService(EstadoTurnoRepository estadoTurnoRepository, EstadoTurnoMapper estadoTurnoMapper) {
        this.estadoTurnoRepository = estadoTurnoRepository;
        this.estadoTurnoMapper = estadoTurnoMapper;
    }

    /**
     * Save a estadoTurno.
     *
     * @param estadoTurnoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoTurnoDTO save(EstadoTurnoDTO estadoTurnoDTO) {
        LOG.debug("Request to save EstadoTurno : {}", estadoTurnoDTO);
        EstadoTurno estadoTurno = estadoTurnoMapper.toEntity(estadoTurnoDTO);
        estadoTurno = estadoTurnoRepository.save(estadoTurno);
        return estadoTurnoMapper.toDto(estadoTurno);
    }

    /**
     * Update a estadoTurno.
     *
     * @param estadoTurnoDTO the entity to save.
     * @return the persisted entity.
     */
    public EstadoTurnoDTO update(EstadoTurnoDTO estadoTurnoDTO) {
        LOG.debug("Request to update EstadoTurno : {}", estadoTurnoDTO);
        EstadoTurno estadoTurno = estadoTurnoMapper.toEntity(estadoTurnoDTO);
        estadoTurno = estadoTurnoRepository.save(estadoTurno);
        return estadoTurnoMapper.toDto(estadoTurno);
    }

    /**
     * Partially update a estadoTurno.
     *
     * @param estadoTurnoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstadoTurnoDTO> partialUpdate(EstadoTurnoDTO estadoTurnoDTO) {
        LOG.debug("Request to partially update EstadoTurno : {}", estadoTurnoDTO);

        return estadoTurnoRepository
            .findById(estadoTurnoDTO.getId())
            .map(existingEstadoTurno -> {
                estadoTurnoMapper.partialUpdate(existingEstadoTurno, estadoTurnoDTO);

                return existingEstadoTurno;
            })
            .map(estadoTurnoRepository::save)
            .map(estadoTurnoMapper::toDto);
    }

    /**
     * Get one estadoTurno by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstadoTurnoDTO> findOne(Long id) {
        LOG.debug("Request to get EstadoTurno : {}", id);
        return estadoTurnoRepository.findById(id).map(estadoTurnoMapper::toDto);
    }

    /**
     * Delete the estadoTurno by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EstadoTurno : {}", id);
        estadoTurnoRepository.deleteById(id);
    }
}
