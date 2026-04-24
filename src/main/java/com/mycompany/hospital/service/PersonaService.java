package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.repository.PersonaRepository;
import com.mycompany.hospital.service.dto.PersonaDTO;
import com.mycompany.hospital.service.mapper.PersonaMapper;
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
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Persona}.
 */
@Service
@Transactional
public class PersonaService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaService.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    /**
     * Save a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonaDTO save(PersonaDTO personaDTO) {
        LOG.debug("Request to save Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    /**
     * Update a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonaDTO update(PersonaDTO personaDTO) {
        LOG.debug("Request to update Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    /**
     * Partially update a persona.
     *
     * @param personaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO) {
        LOG.debug("Request to partially update Persona : {}", personaDTO);

        return personaRepository
            .findById(personaDTO.getId())
            .map(existingPersona -> {
                personaMapper.partialUpdate(existingPersona, personaDTO);

                return existingPersona;
            })
            .map(personaRepository::save)
            .map(personaMapper::toDto);
    }

    /**
     *  Get all the personas where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAllWherePacienteIsNull() {
        LOG.debug("Request to get all personas where Paciente is null");
        return StreamSupport.stream(personaRepository.findAll().spliterator(), false)
            .filter(persona -> persona.getPaciente() == null)
            .map(personaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the personas where Empleado is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAllWhereEmpleadoIsNull() {
        LOG.debug("Request to get all personas where Empleado is null");
        return StreamSupport.stream(personaRepository.findAll().spliterator(), false)
            .filter(persona -> persona.getEmpleado() == null)
            .map(personaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one persona by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findOne(Long id) {
        LOG.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id).map(personaMapper::toDto);
    }

    /**
     * Delete the persona by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Persona : {}", id);
        personaRepository.deleteById(id);
    }
}
