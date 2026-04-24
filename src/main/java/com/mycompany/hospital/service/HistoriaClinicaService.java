package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.repository.HistoriaClinicaRepository;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import com.mycompany.hospital.service.mapper.HistoriaClinicaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.HistoriaClinica}.
 */
@Service
@Transactional
public class HistoriaClinicaService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriaClinicaService.class);

    private final HistoriaClinicaRepository historiaClinicaRepository;

    private final HistoriaClinicaMapper historiaClinicaMapper;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository, HistoriaClinicaMapper historiaClinicaMapper) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.historiaClinicaMapper = historiaClinicaMapper;
    }

    /**
     * Save a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriaClinicaDTO save(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to save HistoriaClinica : {}", historiaClinicaDTO);
        HistoriaClinica historiaClinica = historiaClinicaMapper.toEntity(historiaClinicaDTO);
        historiaClinica = historiaClinicaRepository.save(historiaClinica);
        return historiaClinicaMapper.toDto(historiaClinica);
    }

    /**
     * Update a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriaClinicaDTO update(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to update HistoriaClinica : {}", historiaClinicaDTO);
        HistoriaClinica historiaClinica = historiaClinicaMapper.toEntity(historiaClinicaDTO);
        historiaClinica = historiaClinicaRepository.save(historiaClinica);
        return historiaClinicaMapper.toDto(historiaClinica);
    }

    /**
     * Partially update a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoriaClinicaDTO> partialUpdate(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to partially update HistoriaClinica : {}", historiaClinicaDTO);

        return historiaClinicaRepository
            .findById(historiaClinicaDTO.getId())
            .map(existingHistoriaClinica -> {
                historiaClinicaMapper.partialUpdate(existingHistoriaClinica, historiaClinicaDTO);

                return existingHistoriaClinica;
            })
            .map(historiaClinicaRepository::save)
            .map(historiaClinicaMapper::toDto);
    }

    /**
     * Get one historiaClinica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoriaClinicaDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriaClinica : {}", id);
        return historiaClinicaRepository.findById(id).map(historiaClinicaMapper::toDto);
    }

    /**
     * Delete the historiaClinica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriaClinica : {}", id);
        historiaClinicaRepository.deleteById(id);
    }
}
