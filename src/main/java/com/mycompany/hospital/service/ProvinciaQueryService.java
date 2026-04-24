package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Provincia;
import com.mycompany.hospital.repository.ProvinciaRepository;
import com.mycompany.hospital.service.criteria.ProvinciaCriteria;
import com.mycompany.hospital.service.dto.ProvinciaDTO;
import com.mycompany.hospital.service.mapper.ProvinciaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Provincia} entities in the database.
 * The main input is a {@link ProvinciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProvinciaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvinciaQueryService extends QueryService<Provincia> {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaQueryService.class);

    private final ProvinciaRepository provinciaRepository;

    private final ProvinciaMapper provinciaMapper;

    public ProvinciaQueryService(ProvinciaRepository provinciaRepository, ProvinciaMapper provinciaMapper) {
        this.provinciaRepository = provinciaRepository;
        this.provinciaMapper = provinciaMapper;
    }

    /**
     * Return a {@link Page} of {@link ProvinciaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvinciaDTO> findByCriteria(ProvinciaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Provincia> specification = createSpecification(criteria);
        return provinciaRepository.findAll(specification, page).map(provinciaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProvinciaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Provincia> specification = createSpecification(criteria);
        return provinciaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProvinciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Provincia> createSpecification(ProvinciaCriteria criteria) {
        Specification<Provincia> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Provincia_.id),
                buildStringSpecification(criteria.getNombre(), Provincia_.nombre),
                buildStringSpecification(criteria.getCodigo(), Provincia_.codigo),
                buildRangeSpecification(criteria.getFechaAlta(), Provincia_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Provincia_.fechaBaja),
                buildSpecification(criteria.getActivo(), Provincia_.activo),
                buildSpecification(criteria.getPaisId(), root -> root.join(Provincia_.pais, JoinType.LEFT).get(Pais_.id))
            );
        }
        return specification;
    }
}
