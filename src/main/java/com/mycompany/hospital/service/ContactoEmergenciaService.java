package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.ContactoEmergencia;
import com.mycompany.hospital.repository.ContactoEmergenciaRepository;
import com.mycompany.hospital.service.dto.ContactoEmergenciaDTO;
import com.mycompany.hospital.service.mapper.ContactoEmergenciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.ContactoEmergencia}.
 */
@Service
@Transactional
public class ContactoEmergenciaService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactoEmergenciaService.class);

    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    private final ContactoEmergenciaMapper contactoEmergenciaMapper;

    public ContactoEmergenciaService(
        ContactoEmergenciaRepository contactoEmergenciaRepository,
        ContactoEmergenciaMapper contactoEmergenciaMapper
    ) {
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
        this.contactoEmergenciaMapper = contactoEmergenciaMapper;
    }

    /**
     * Save a contactoEmergencia.
     *
     * @param contactoEmergenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactoEmergenciaDTO save(ContactoEmergenciaDTO contactoEmergenciaDTO) {
        LOG.debug("Request to save ContactoEmergencia : {}", contactoEmergenciaDTO);
        ContactoEmergencia contactoEmergencia = contactoEmergenciaMapper.toEntity(contactoEmergenciaDTO);
        contactoEmergencia = contactoEmergenciaRepository.save(contactoEmergencia);
        return contactoEmergenciaMapper.toDto(contactoEmergencia);
    }

    /**
     * Update a contactoEmergencia.
     *
     * @param contactoEmergenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactoEmergenciaDTO update(ContactoEmergenciaDTO contactoEmergenciaDTO) {
        LOG.debug("Request to update ContactoEmergencia : {}", contactoEmergenciaDTO);
        ContactoEmergencia contactoEmergencia = contactoEmergenciaMapper.toEntity(contactoEmergenciaDTO);
        contactoEmergencia = contactoEmergenciaRepository.save(contactoEmergencia);
        return contactoEmergenciaMapper.toDto(contactoEmergencia);
    }

    /**
     * Partially update a contactoEmergencia.
     *
     * @param contactoEmergenciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactoEmergenciaDTO> partialUpdate(ContactoEmergenciaDTO contactoEmergenciaDTO) {
        LOG.debug("Request to partially update ContactoEmergencia : {}", contactoEmergenciaDTO);

        return contactoEmergenciaRepository
            .findById(contactoEmergenciaDTO.getId())
            .map(existingContactoEmergencia -> {
                contactoEmergenciaMapper.partialUpdate(existingContactoEmergencia, contactoEmergenciaDTO);

                return existingContactoEmergencia;
            })
            .map(contactoEmergenciaRepository::save)
            .map(contactoEmergenciaMapper::toDto);
    }

    /**
     * Get one contactoEmergencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactoEmergenciaDTO> findOne(Long id) {
        LOG.debug("Request to get ContactoEmergencia : {}", id);
        return contactoEmergenciaRepository.findById(id).map(contactoEmergenciaMapper::toDto);
    }

    /**
     * Delete the contactoEmergencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactoEmergencia : {}", id);
        contactoEmergenciaRepository.deleteById(id);
    }
}
