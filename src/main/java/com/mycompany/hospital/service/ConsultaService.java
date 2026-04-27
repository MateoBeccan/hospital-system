package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.repository.ConsultaRepository;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.mapper.ConsultaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Consulta}.
 */
@Service
@Transactional
public class ConsultaService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultaService.class);

    private final ConsultaRepository consultaRepository;

    private final ConsultaMapper consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository, ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.consultaMapper = consultaMapper;
    }

    /**
     * Save a consulta.
     *
     * @param consultaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultaDTO save(ConsultaDTO consultaDTO) {
        LOG.debug("Request to save Consulta : {}", consultaDTO);
        Consulta consulta = consultaMapper.toEntity(consultaDTO);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toDto(consulta);
    }

    /**
     * Update a consulta.
     *
     * @param consultaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultaDTO update(ConsultaDTO consultaDTO) {
        LOG.debug("Request to update Consulta : {}", consultaDTO);
        Consulta consulta = consultaMapper.toEntity(consultaDTO);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toDto(consulta);
    }

    /**
     * Partially update a consulta.
     *
     * @param consultaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsultaDTO> partialUpdate(ConsultaDTO consultaDTO) {
        LOG.debug("Request to partially update Consulta : {}", consultaDTO);

        return consultaRepository
            .findById(consultaDTO.getId())
            .map(existingConsulta -> {
                consultaMapper.partialUpdate(existingConsulta, consultaDTO);

                return existingConsulta;
            })
            .map(consultaRepository::save)
            .map(consultaMapper::toDto);
    }

    /**
     * Get one consulta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsultaDTO> findOne(Long id) {
        LOG.debug("Request to get Consulta : {}", id);
        return consultaRepository.findById(id).map(consultaMapper::toDto);
    }

    /**
     * Delete the consulta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Consulta : {}", id);
        consultaRepository.deleteById(id);
    }
}
