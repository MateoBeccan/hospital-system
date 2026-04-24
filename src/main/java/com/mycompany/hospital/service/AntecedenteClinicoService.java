package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.AntecedenteClinico;
import com.mycompany.hospital.repository.AntecedenteClinicoRepository;
import com.mycompany.hospital.service.dto.AntecedenteClinicoDTO;
import com.mycompany.hospital.service.mapper.AntecedenteClinicoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.AntecedenteClinico}.
 */
@Service
@Transactional
public class AntecedenteClinicoService {

    private static final Logger LOG = LoggerFactory.getLogger(AntecedenteClinicoService.class);

    private final AntecedenteClinicoRepository antecedenteClinicoRepository;

    private final AntecedenteClinicoMapper antecedenteClinicoMapper;

    public AntecedenteClinicoService(
        AntecedenteClinicoRepository antecedenteClinicoRepository,
        AntecedenteClinicoMapper antecedenteClinicoMapper
    ) {
        this.antecedenteClinicoRepository = antecedenteClinicoRepository;
        this.antecedenteClinicoMapper = antecedenteClinicoMapper;
    }

    /**
     * Save a antecedenteClinico.
     *
     * @param antecedenteClinicoDTO the entity to save.
     * @return the persisted entity.
     */
    public AntecedenteClinicoDTO save(AntecedenteClinicoDTO antecedenteClinicoDTO) {
        LOG.debug("Request to save AntecedenteClinico : {}", antecedenteClinicoDTO);
        AntecedenteClinico antecedenteClinico = antecedenteClinicoMapper.toEntity(antecedenteClinicoDTO);
        antecedenteClinico = antecedenteClinicoRepository.save(antecedenteClinico);
        return antecedenteClinicoMapper.toDto(antecedenteClinico);
    }

    /**
     * Update a antecedenteClinico.
     *
     * @param antecedenteClinicoDTO the entity to save.
     * @return the persisted entity.
     */
    public AntecedenteClinicoDTO update(AntecedenteClinicoDTO antecedenteClinicoDTO) {
        LOG.debug("Request to update AntecedenteClinico : {}", antecedenteClinicoDTO);
        AntecedenteClinico antecedenteClinico = antecedenteClinicoMapper.toEntity(antecedenteClinicoDTO);
        antecedenteClinico = antecedenteClinicoRepository.save(antecedenteClinico);
        return antecedenteClinicoMapper.toDto(antecedenteClinico);
    }

    /**
     * Partially update a antecedenteClinico.
     *
     * @param antecedenteClinicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AntecedenteClinicoDTO> partialUpdate(AntecedenteClinicoDTO antecedenteClinicoDTO) {
        LOG.debug("Request to partially update AntecedenteClinico : {}", antecedenteClinicoDTO);

        return antecedenteClinicoRepository
            .findById(antecedenteClinicoDTO.getId())
            .map(existingAntecedenteClinico -> {
                antecedenteClinicoMapper.partialUpdate(existingAntecedenteClinico, antecedenteClinicoDTO);

                return existingAntecedenteClinico;
            })
            .map(antecedenteClinicoRepository::save)
            .map(antecedenteClinicoMapper::toDto);
    }

    /**
     * Get one antecedenteClinico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AntecedenteClinicoDTO> findOne(Long id) {
        LOG.debug("Request to get AntecedenteClinico : {}", id);
        return antecedenteClinicoRepository.findById(id).map(antecedenteClinicoMapper::toDto);
    }

    /**
     * Delete the antecedenteClinico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AntecedenteClinico : {}", id);
        antecedenteClinicoRepository.deleteById(id);
    }
}
