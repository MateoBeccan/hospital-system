package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.repository.ObraSocialRepository;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
import com.mycompany.hospital.service.mapper.ObraSocialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.ObraSocial}.
 */
@Service
@Transactional
public class ObraSocialService {

    private static final Logger LOG = LoggerFactory.getLogger(ObraSocialService.class);

    private final ObraSocialRepository obraSocialRepository;

    private final ObraSocialMapper obraSocialMapper;

    public ObraSocialService(ObraSocialRepository obraSocialRepository, ObraSocialMapper obraSocialMapper) {
        this.obraSocialRepository = obraSocialRepository;
        this.obraSocialMapper = obraSocialMapper;
    }

    /**
     * Save a obraSocial.
     *
     * @param obraSocialDTO the entity to save.
     * @return the persisted entity.
     */
    public ObraSocialDTO save(ObraSocialDTO obraSocialDTO) {
        LOG.debug("Request to save ObraSocial : {}", obraSocialDTO);
        ObraSocial obraSocial = obraSocialMapper.toEntity(obraSocialDTO);
        obraSocial = obraSocialRepository.save(obraSocial);
        return obraSocialMapper.toDto(obraSocial);
    }

    /**
     * Update a obraSocial.
     *
     * @param obraSocialDTO the entity to save.
     * @return the persisted entity.
     */
    public ObraSocialDTO update(ObraSocialDTO obraSocialDTO) {
        LOG.debug("Request to update ObraSocial : {}", obraSocialDTO);
        ObraSocial obraSocial = obraSocialMapper.toEntity(obraSocialDTO);
        obraSocial = obraSocialRepository.save(obraSocial);
        return obraSocialMapper.toDto(obraSocial);
    }

    /**
     * Partially update a obraSocial.
     *
     * @param obraSocialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ObraSocialDTO> partialUpdate(ObraSocialDTO obraSocialDTO) {
        LOG.debug("Request to partially update ObraSocial : {}", obraSocialDTO);

        return obraSocialRepository
            .findById(obraSocialDTO.getId())
            .map(existingObraSocial -> {
                obraSocialMapper.partialUpdate(existingObraSocial, obraSocialDTO);

                return existingObraSocial;
            })
            .map(obraSocialRepository::save)
            .map(obraSocialMapper::toDto);
    }

    /**
     * Get one obraSocial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ObraSocialDTO> findOne(Long id) {
        LOG.debug("Request to get ObraSocial : {}", id);
        return obraSocialRepository.findById(id).map(obraSocialMapper::toDto);
    }

    /**
     * Delete the obraSocial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ObraSocial : {}", id);
        obraSocialRepository.deleteById(id);
    }
}
