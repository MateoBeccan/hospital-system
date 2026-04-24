package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Sexo;
import com.mycompany.hospital.repository.SexoRepository;
import com.mycompany.hospital.service.dto.SexoDTO;
import com.mycompany.hospital.service.mapper.SexoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Sexo}.
 */
@Service
@Transactional
public class SexoService {

    private static final Logger LOG = LoggerFactory.getLogger(SexoService.class);

    private final SexoRepository sexoRepository;

    private final SexoMapper sexoMapper;

    public SexoService(SexoRepository sexoRepository, SexoMapper sexoMapper) {
        this.sexoRepository = sexoRepository;
        this.sexoMapper = sexoMapper;
    }

    /**
     * Save a sexo.
     *
     * @param sexoDTO the entity to save.
     * @return the persisted entity.
     */
    public SexoDTO save(SexoDTO sexoDTO) {
        LOG.debug("Request to save Sexo : {}", sexoDTO);
        Sexo sexo = sexoMapper.toEntity(sexoDTO);
        sexo = sexoRepository.save(sexo);
        return sexoMapper.toDto(sexo);
    }

    /**
     * Update a sexo.
     *
     * @param sexoDTO the entity to save.
     * @return the persisted entity.
     */
    public SexoDTO update(SexoDTO sexoDTO) {
        LOG.debug("Request to update Sexo : {}", sexoDTO);
        Sexo sexo = sexoMapper.toEntity(sexoDTO);
        sexo = sexoRepository.save(sexo);
        return sexoMapper.toDto(sexo);
    }

    /**
     * Partially update a sexo.
     *
     * @param sexoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SexoDTO> partialUpdate(SexoDTO sexoDTO) {
        LOG.debug("Request to partially update Sexo : {}", sexoDTO);

        return sexoRepository
            .findById(sexoDTO.getId())
            .map(existingSexo -> {
                sexoMapper.partialUpdate(existingSexo, sexoDTO);

                return existingSexo;
            })
            .map(sexoRepository::save)
            .map(sexoMapper::toDto);
    }

    /**
     * Get one sexo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SexoDTO> findOne(Long id) {
        LOG.debug("Request to get Sexo : {}", id);
        return sexoRepository.findById(id).map(sexoMapper::toDto);
    }

    /**
     * Delete the sexo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sexo : {}", id);
        sexoRepository.deleteById(id);
    }
}
