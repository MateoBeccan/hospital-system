package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.repository.EspecialidadRepository;
import com.mycompany.hospital.service.dto.EspecialidadDTO;
import com.mycompany.hospital.service.mapper.EspecialidadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Especialidad}.
 */
@Service
@Transactional
public class EspecialidadService {

    private static final Logger LOG = LoggerFactory.getLogger(EspecialidadService.class);

    private final EspecialidadRepository especialidadRepository;

    private final EspecialidadMapper especialidadMapper;

    public EspecialidadService(EspecialidadRepository especialidadRepository, EspecialidadMapper especialidadMapper) {
        this.especialidadRepository = especialidadRepository;
        this.especialidadMapper = especialidadMapper;
    }

    /**
     * Save a especialidad.
     *
     * @param especialidadDTO the entity to save.
     * @return the persisted entity.
     */
    public EspecialidadDTO save(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to save Especialidad : {}", especialidadDTO);
        Especialidad especialidad = especialidadMapper.toEntity(especialidadDTO);
        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(especialidad);
    }

    /**
     * Update a especialidad.
     *
     * @param especialidadDTO the entity to save.
     * @return the persisted entity.
     */
    public EspecialidadDTO update(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to update Especialidad : {}", especialidadDTO);
        Especialidad especialidad = especialidadMapper.toEntity(especialidadDTO);
        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(especialidad);
    }

    /**
     * Partially update a especialidad.
     *
     * @param especialidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EspecialidadDTO> partialUpdate(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to partially update Especialidad : {}", especialidadDTO);

        return especialidadRepository
            .findById(especialidadDTO.getId())
            .map(existingEspecialidad -> {
                especialidadMapper.partialUpdate(existingEspecialidad, especialidadDTO);

                return existingEspecialidad;
            })
            .map(especialidadRepository::save)
            .map(especialidadMapper::toDto);
    }

    /**
     * Get one especialidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EspecialidadDTO> findOne(Long id) {
        LOG.debug("Request to get Especialidad : {}", id);
        return especialidadRepository.findById(id).map(especialidadMapper::toDto);
    }

    /**
     * Delete the especialidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Especialidad : {}", id);
        especialidadRepository.deleteById(id);
    }
}
