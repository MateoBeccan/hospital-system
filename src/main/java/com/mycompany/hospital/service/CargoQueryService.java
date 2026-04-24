package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.repository.CargoRepository;
import com.mycompany.hospital.service.criteria.CargoCriteria;
import com.mycompany.hospital.service.dto.CargoDTO;
import com.mycompany.hospital.service.mapper.CargoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cargo} entities in the database.
 * The main input is a {@link CargoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CargoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CargoQueryService extends QueryService<Cargo> {

    private static final Logger LOG = LoggerFactory.getLogger(CargoQueryService.class);

    private final CargoRepository cargoRepository;

    private final CargoMapper cargoMapper;

    public CargoQueryService(CargoRepository cargoRepository, CargoMapper cargoMapper) {
        this.cargoRepository = cargoRepository;
        this.cargoMapper = cargoMapper;
    }

    /**
     * Return a {@link Page} of {@link CargoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoDTO> findByCriteria(CargoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cargo> specification = createSpecification(criteria);
        return cargoRepository.findAll(specification, page).map(cargoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CargoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cargo> specification = createSpecification(criteria);
        return cargoRepository.count(specification);
    }

    /**
     * Function to convert {@link CargoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cargo> createSpecification(CargoCriteria criteria) {
        Specification<Cargo> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Cargo_.id),
                buildStringSpecification(criteria.getCodigo(), Cargo_.codigo),
                buildStringSpecification(criteria.getNombre(), Cargo_.nombre),
                buildStringSpecification(criteria.getDescripcion(), Cargo_.descripcion),
                buildSpecification(criteria.getActivo(), Cargo_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), Cargo_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Cargo_.fechaBaja)
            );
        }
        return specification;
    }
}
