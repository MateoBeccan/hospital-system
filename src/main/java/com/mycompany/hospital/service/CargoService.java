package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.repository.CargoRepository;
import com.mycompany.hospital.service.dto.CargoDTO;
import com.mycompany.hospital.service.mapper.CargoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.hospital.domain.Cargo}.
 */
@Service
@Transactional
public class CargoService {

    private static final Logger LOG = LoggerFactory.getLogger(CargoService.class);

    private final CargoRepository cargoRepository;

    private final CargoMapper cargoMapper;

    public CargoService(CargoRepository cargoRepository, CargoMapper cargoMapper) {
        this.cargoRepository = cargoRepository;
        this.cargoMapper = cargoMapper;
    }

    /**
     * Save a cargo.
     *
     * @param cargoDTO the entity to save.
     * @return the persisted entity.
     */
    public CargoDTO save(CargoDTO cargoDTO) {
        LOG.debug("Request to save Cargo : {}", cargoDTO);
        Cargo cargo = cargoMapper.toEntity(cargoDTO);
        cargo = cargoRepository.save(cargo);
        return cargoMapper.toDto(cargo);
    }

    /**
     * Update a cargo.
     *
     * @param cargoDTO the entity to save.
     * @return the persisted entity.
     */
    public CargoDTO update(CargoDTO cargoDTO) {
        LOG.debug("Request to update Cargo : {}", cargoDTO);
        Cargo cargo = cargoMapper.toEntity(cargoDTO);
        cargo = cargoRepository.save(cargo);
        return cargoMapper.toDto(cargo);
    }

    /**
     * Partially update a cargo.
     *
     * @param cargoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CargoDTO> partialUpdate(CargoDTO cargoDTO) {
        LOG.debug("Request to partially update Cargo : {}", cargoDTO);

        return cargoRepository
            .findById(cargoDTO.getId())
            .map(existingCargo -> {
                cargoMapper.partialUpdate(existingCargo, cargoDTO);

                return existingCargo;
            })
            .map(cargoRepository::save)
            .map(cargoMapper::toDto);
    }

    /**
     * Get one cargo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CargoDTO> findOne(Long id) {
        LOG.debug("Request to get Cargo : {}", id);
        return cargoRepository.findById(id).map(cargoMapper::toDto);
    }

    /**
     * Delete the cargo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cargo : {}", id);
        cargoRepository.deleteById(id);
    }
}
