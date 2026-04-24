package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.repository.TipoEmpleadoRepository;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
import com.mycompany.hospital.service.mapper.TipoEmpleadoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.TipoEmpleado}.
 */
@Service
@Transactional
public class TipoEmpleadoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEmpleadoService.class);

    private final TipoEmpleadoRepository tipoEmpleadoRepository;

    private final TipoEmpleadoMapper tipoEmpleadoMapper;

    public TipoEmpleadoService(TipoEmpleadoRepository tipoEmpleadoRepository, TipoEmpleadoMapper tipoEmpleadoMapper) {
        this.tipoEmpleadoRepository = tipoEmpleadoRepository;
        this.tipoEmpleadoMapper = tipoEmpleadoMapper;
    }

    /**
     * Save a tipoEmpleado.
     *
     * @param tipoEmpleadoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoEmpleadoDTO save(TipoEmpleadoDTO tipoEmpleadoDTO) {
        LOG.debug("Request to save TipoEmpleado : {}", tipoEmpleadoDTO);
        TipoEmpleado tipoEmpleado = tipoEmpleadoMapper.toEntity(tipoEmpleadoDTO);
        tipoEmpleado = tipoEmpleadoRepository.save(tipoEmpleado);
        return tipoEmpleadoMapper.toDto(tipoEmpleado);
    }

    /**
     * Update a tipoEmpleado.
     *
     * @param tipoEmpleadoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoEmpleadoDTO update(TipoEmpleadoDTO tipoEmpleadoDTO) {
        LOG.debug("Request to update TipoEmpleado : {}", tipoEmpleadoDTO);
        TipoEmpleado tipoEmpleado = tipoEmpleadoMapper.toEntity(tipoEmpleadoDTO);
        tipoEmpleado = tipoEmpleadoRepository.save(tipoEmpleado);
        return tipoEmpleadoMapper.toDto(tipoEmpleado);
    }

    /**
     * Partially update a tipoEmpleado.
     *
     * @param tipoEmpleadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoEmpleadoDTO> partialUpdate(TipoEmpleadoDTO tipoEmpleadoDTO) {
        LOG.debug("Request to partially update TipoEmpleado : {}", tipoEmpleadoDTO);

        return tipoEmpleadoRepository
            .findById(tipoEmpleadoDTO.getId())
            .map(existingTipoEmpleado -> {
                tipoEmpleadoMapper.partialUpdate(existingTipoEmpleado, tipoEmpleadoDTO);

                return existingTipoEmpleado;
            })
            .map(tipoEmpleadoRepository::save)
            .map(tipoEmpleadoMapper::toDto);
    }

    /**
     * Get one tipoEmpleado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoEmpleadoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoEmpleado : {}", id);
        return tipoEmpleadoRepository.findById(id).map(tipoEmpleadoMapper::toDto);
    }

    /**
     * Delete the tipoEmpleado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoEmpleado : {}", id);
        tipoEmpleadoRepository.deleteById(id);
    }
}
