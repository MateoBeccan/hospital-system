package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.repository.GrupoSanguineoRepository;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
import com.mycompany.hospital.service.mapper.GrupoSanguineoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.GrupoSanguineo}.
 */
@Service
@Transactional
public class GrupoSanguineoService {

    private static final Logger LOG = LoggerFactory.getLogger(GrupoSanguineoService.class);

    private final GrupoSanguineoRepository grupoSanguineoRepository;

    private final GrupoSanguineoMapper grupoSanguineoMapper;

    public GrupoSanguineoService(GrupoSanguineoRepository grupoSanguineoRepository, GrupoSanguineoMapper grupoSanguineoMapper) {
        this.grupoSanguineoRepository = grupoSanguineoRepository;
        this.grupoSanguineoMapper = grupoSanguineoMapper;
    }

    /**
     * Save a grupoSanguineo.
     *
     * @param grupoSanguineoDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoSanguineoDTO save(GrupoSanguineoDTO grupoSanguineoDTO) {
        LOG.debug("Request to save GrupoSanguineo : {}", grupoSanguineoDTO);
        GrupoSanguineo grupoSanguineo = grupoSanguineoMapper.toEntity(grupoSanguineoDTO);
        grupoSanguineo = grupoSanguineoRepository.save(grupoSanguineo);
        return grupoSanguineoMapper.toDto(grupoSanguineo);
    }

    /**
     * Update a grupoSanguineo.
     *
     * @param grupoSanguineoDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoSanguineoDTO update(GrupoSanguineoDTO grupoSanguineoDTO) {
        LOG.debug("Request to update GrupoSanguineo : {}", grupoSanguineoDTO);
        GrupoSanguineo grupoSanguineo = grupoSanguineoMapper.toEntity(grupoSanguineoDTO);
        grupoSanguineo = grupoSanguineoRepository.save(grupoSanguineo);
        return grupoSanguineoMapper.toDto(grupoSanguineo);
    }

    /**
     * Partially update a grupoSanguineo.
     *
     * @param grupoSanguineoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GrupoSanguineoDTO> partialUpdate(GrupoSanguineoDTO grupoSanguineoDTO) {
        LOG.debug("Request to partially update GrupoSanguineo : {}", grupoSanguineoDTO);

        return grupoSanguineoRepository
            .findById(grupoSanguineoDTO.getId())
            .map(existingGrupoSanguineo -> {
                grupoSanguineoMapper.partialUpdate(existingGrupoSanguineo, grupoSanguineoDTO);

                return existingGrupoSanguineo;
            })
            .map(grupoSanguineoRepository::save)
            .map(grupoSanguineoMapper::toDto);
    }

    /**
     * Get one grupoSanguineo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GrupoSanguineoDTO> findOne(Long id) {
        LOG.debug("Request to get GrupoSanguineo : {}", id);
        return grupoSanguineoRepository.findById(id).map(grupoSanguineoMapper::toDto);
    }

    /**
     * Delete the grupoSanguineo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GrupoSanguineo : {}", id);
        grupoSanguineoRepository.deleteById(id);
    }
}
