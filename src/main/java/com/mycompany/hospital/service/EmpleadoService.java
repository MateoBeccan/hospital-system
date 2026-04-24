package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.repository.EmpleadoRepository;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.mapper.EmpleadoMapper;
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
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Empleado}.
 */
@Service
@Transactional
public class EmpleadoService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoService.class);

    private final EmpleadoRepository empleadoRepository;

    private final EmpleadoMapper empleadoMapper;

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
    }

    /**
     * Save a empleado.
     *
     * @param empleadoDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpleadoDTO save(EmpleadoDTO empleadoDTO) {
        LOG.debug("Request to save Empleado : {}", empleadoDTO);
        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado = empleadoRepository.save(empleado);
        return empleadoMapper.toDto(empleado);
    }

    /**
     * Update a empleado.
     *
     * @param empleadoDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpleadoDTO update(EmpleadoDTO empleadoDTO) {
        LOG.debug("Request to update Empleado : {}", empleadoDTO);
        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado = empleadoRepository.save(empleado);
        return empleadoMapper.toDto(empleado);
    }

    /**
     * Partially update a empleado.
     *
     * @param empleadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmpleadoDTO> partialUpdate(EmpleadoDTO empleadoDTO) {
        LOG.debug("Request to partially update Empleado : {}", empleadoDTO);

        return empleadoRepository
            .findById(empleadoDTO.getId())
            .map(existingEmpleado -> {
                empleadoMapper.partialUpdate(existingEmpleado, empleadoDTO);

                return existingEmpleado;
            })
            .map(empleadoRepository::save)
            .map(empleadoMapper::toDto);
    }

    /**
     *  Get all the empleados where Medico is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmpleadoDTO> findAllWhereMedicoIsNull() {
        LOG.debug("Request to get all empleados where Medico is null");
        return StreamSupport.stream(empleadoRepository.findAll().spliterator(), false)
            .filter(empleado -> empleado.getMedico() == null)
            .map(empleadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the empleados where Enfermero is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmpleadoDTO> findAllWhereEnfermeroIsNull() {
        LOG.debug("Request to get all empleados where Enfermero is null");
        return StreamSupport.stream(empleadoRepository.findAll().spliterator(), false)
            .filter(empleado -> empleado.getEnfermero() == null)
            .map(empleadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one empleado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmpleadoDTO> findOne(Long id) {
        LOG.debug("Request to get Empleado : {}", id);
        return empleadoRepository.findById(id).map(empleadoMapper::toDto);
    }

    /**
     * Delete the empleado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Empleado : {}", id);
        empleadoRepository.deleteById(id);
    }
}
