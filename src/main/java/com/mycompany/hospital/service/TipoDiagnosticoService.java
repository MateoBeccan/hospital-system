package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.repository.TipoDiagnosticoRepository;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.TipoDiagnosticoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.TipoDiagnostico}.
 */
@Service
@Transactional
public class TipoDiagnosticoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDiagnosticoService.class);

    private final TipoDiagnosticoRepository tipoDiagnosticoRepository;

    private final TipoDiagnosticoMapper tipoDiagnosticoMapper;

    public TipoDiagnosticoService(TipoDiagnosticoRepository tipoDiagnosticoRepository, TipoDiagnosticoMapper tipoDiagnosticoMapper) {
        this.tipoDiagnosticoRepository = tipoDiagnosticoRepository;
        this.tipoDiagnosticoMapper = tipoDiagnosticoMapper;
    }

    /**
     * Save a tipoDiagnostico.
     *
     * @param tipoDiagnosticoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDiagnosticoDTO save(TipoDiagnosticoDTO tipoDiagnosticoDTO) {
        LOG.debug("Request to save TipoDiagnostico : {}", tipoDiagnosticoDTO);
        TipoDiagnostico tipoDiagnostico = tipoDiagnosticoMapper.toEntity(tipoDiagnosticoDTO);
        tipoDiagnostico = tipoDiagnosticoRepository.save(tipoDiagnostico);
        return tipoDiagnosticoMapper.toDto(tipoDiagnostico);
    }

    /**
     * Update a tipoDiagnostico.
     *
     * @param tipoDiagnosticoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDiagnosticoDTO update(TipoDiagnosticoDTO tipoDiagnosticoDTO) {
        LOG.debug("Request to update TipoDiagnostico : {}", tipoDiagnosticoDTO);
        TipoDiagnostico tipoDiagnostico = tipoDiagnosticoMapper.toEntity(tipoDiagnosticoDTO);
        tipoDiagnostico = tipoDiagnosticoRepository.save(tipoDiagnostico);
        return tipoDiagnosticoMapper.toDto(tipoDiagnostico);
    }

    /**
     * Partially update a tipoDiagnostico.
     *
     * @param tipoDiagnosticoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoDiagnosticoDTO> partialUpdate(TipoDiagnosticoDTO tipoDiagnosticoDTO) {
        LOG.debug("Request to partially update TipoDiagnostico : {}", tipoDiagnosticoDTO);

        return tipoDiagnosticoRepository
            .findById(tipoDiagnosticoDTO.getId())
            .map(existingTipoDiagnostico -> {
                tipoDiagnosticoMapper.partialUpdate(existingTipoDiagnostico, tipoDiagnosticoDTO);

                return existingTipoDiagnostico;
            })
            .map(tipoDiagnosticoRepository::save)
            .map(tipoDiagnosticoMapper::toDto);
    }

    /**
     * Get one tipoDiagnostico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoDiagnosticoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoDiagnostico : {}", id);
        return tipoDiagnosticoRepository.findById(id).map(tipoDiagnosticoMapper::toDto);
    }

    /**
     * Delete the tipoDiagnostico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoDiagnostico : {}", id);
        tipoDiagnosticoRepository.deleteById(id);
    }
}
