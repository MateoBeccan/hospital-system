package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.repository.PacienteRepository;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.mapper.PacienteMapper;
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
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Paciente}.
 */
@Service
@Transactional
public class PacienteService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Save a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    public PacienteDTO save(PacienteDTO pacienteDTO) {
        LOG.debug("Request to save Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    /**
     * Update a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    public PacienteDTO update(PacienteDTO pacienteDTO) {
        LOG.debug("Request to update Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    /**
     * Partially update a paciente.
     *
     * @param pacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        LOG.debug("Request to partially update Paciente : {}", pacienteDTO);

        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(existingPaciente -> {
                pacienteMapper.partialUpdate(existingPaciente, pacienteDTO);

                return existingPaciente;
            })
            .map(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    /**
     *  Get all the pacientes where HistoriaClinica is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAllWhereHistoriaClinicaIsNull() {
        LOG.debug("Request to get all pacientes where HistoriaClinica is null");
        return StreamSupport.stream(pacienteRepository.findAll().spliterator(), false)
            .filter(paciente -> paciente.getHistoriaClinica() == null)
            .map(pacienteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paciente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findOne(Long id) {
        LOG.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id).map(pacienteMapper::toDto);
    }

    /**
     * Delete the paciente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
