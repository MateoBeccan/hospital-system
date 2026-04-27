package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.CanalSolicitud;
import com.mycompany.hospital.repository.CanalSolicitudRepository;
import com.mycompany.hospital.service.dto.CanalSolicitudDTO;
import com.mycompany.hospital.service.mapper.CanalSolicitudMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.CanalSolicitud}.
 */
@Service
@Transactional
public class CanalSolicitudService {

    private static final Logger LOG = LoggerFactory.getLogger(CanalSolicitudService.class);

    private final CanalSolicitudRepository canalSolicitudRepository;

    private final CanalSolicitudMapper canalSolicitudMapper;

    public CanalSolicitudService(CanalSolicitudRepository canalSolicitudRepository, CanalSolicitudMapper canalSolicitudMapper) {
        this.canalSolicitudRepository = canalSolicitudRepository;
        this.canalSolicitudMapper = canalSolicitudMapper;
    }

    /**
     * Save a canalSolicitud.
     *
     * @param canalSolicitudDTO the entity to save.
     * @return the persisted entity.
     */
    public CanalSolicitudDTO save(CanalSolicitudDTO canalSolicitudDTO) {
        LOG.debug("Request to save CanalSolicitud : {}", canalSolicitudDTO);
        CanalSolicitud canalSolicitud = canalSolicitudMapper.toEntity(canalSolicitudDTO);
        canalSolicitud = canalSolicitudRepository.save(canalSolicitud);
        return canalSolicitudMapper.toDto(canalSolicitud);
    }

    /**
     * Update a canalSolicitud.
     *
     * @param canalSolicitudDTO the entity to save.
     * @return the persisted entity.
     */
    public CanalSolicitudDTO update(CanalSolicitudDTO canalSolicitudDTO) {
        LOG.debug("Request to update CanalSolicitud : {}", canalSolicitudDTO);
        CanalSolicitud canalSolicitud = canalSolicitudMapper.toEntity(canalSolicitudDTO);
        canalSolicitud = canalSolicitudRepository.save(canalSolicitud);
        return canalSolicitudMapper.toDto(canalSolicitud);
    }

    /**
     * Partially update a canalSolicitud.
     *
     * @param canalSolicitudDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CanalSolicitudDTO> partialUpdate(CanalSolicitudDTO canalSolicitudDTO) {
        LOG.debug("Request to partially update CanalSolicitud : {}", canalSolicitudDTO);

        return canalSolicitudRepository
            .findById(canalSolicitudDTO.getId())
            .map(existingCanalSolicitud -> {
                canalSolicitudMapper.partialUpdate(existingCanalSolicitud, canalSolicitudDTO);

                return existingCanalSolicitud;
            })
            .map(canalSolicitudRepository::save)
            .map(canalSolicitudMapper::toDto);
    }

    /**
     * Get one canalSolicitud by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CanalSolicitudDTO> findOne(Long id) {
        LOG.debug("Request to get CanalSolicitud : {}", id);
        return canalSolicitudRepository.findById(id).map(canalSolicitudMapper::toDto);
    }

    /**
     * Delete the canalSolicitud by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CanalSolicitud : {}", id);
        canalSolicitudRepository.deleteById(id);
    }
}
