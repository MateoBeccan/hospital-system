package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.repository.TurnoRepository;
import com.mycompany.hospital.service.dto.TurnoDTO;
import com.mycompany.hospital.service.mapper.TurnoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Turno}.
 */
@Service
@Transactional
public class TurnoService {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoService.class);

    private final TurnoRepository turnoRepository;

    private final TurnoMapper turnoMapper;

    public TurnoService(TurnoRepository turnoRepository, TurnoMapper turnoMapper) {
        this.turnoRepository = turnoRepository;
        this.turnoMapper = turnoMapper;
    }

    /**
     * Save a turno.
     *
     * @param turnoDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoDTO save(TurnoDTO turnoDTO) {
        LOG.debug("Request to save Turno : {}", turnoDTO);
        Turno turno = turnoMapper.toEntity(turnoDTO);
        turno = turnoRepository.save(turno);
        return turnoMapper.toDto(turno);
    }

    /**
     * Update a turno.
     *
     * @param turnoDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoDTO update(TurnoDTO turnoDTO) {
        LOG.debug("Request to update Turno : {}", turnoDTO);
        Turno turno = turnoMapper.toEntity(turnoDTO);
        turno = turnoRepository.save(turno);
        return turnoMapper.toDto(turno);
    }

    /**
     * Partially update a turno.
     *
     * @param turnoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TurnoDTO> partialUpdate(TurnoDTO turnoDTO) {
        LOG.debug("Request to partially update Turno : {}", turnoDTO);

        return turnoRepository
            .findById(turnoDTO.getId())
            .map(existingTurno -> {
                turnoMapper.partialUpdate(existingTurno, turnoDTO);

                return existingTurno;
            })
            .map(turnoRepository::save)
            .map(turnoMapper::toDto);
    }

    /**
     *  Get all the turnos where Consulta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TurnoDTO> findAllWhereConsultaIsNull() {
        LOG.debug("Request to get all turnos where Consulta is null");
        return StreamSupport.stream(turnoRepository.findAll().spliterator(), false)
            .filter(turno -> turno.getConsulta() == null)
            .map(turnoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one turno by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurnoDTO> findOne(Long id) {
        LOG.debug("Request to get Turno : {}", id);
        return turnoRepository.findById(id).map(turnoMapper::toDto);
    }

    /**
     * Delete the turno by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Turno : {}", id);
        turnoRepository.deleteById(id);
    }
}
