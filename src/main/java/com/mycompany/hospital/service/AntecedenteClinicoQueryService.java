package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.AntecedenteClinico;
import com.mycompany.hospital.repository.AntecedenteClinicoRepository;
import com.mycompany.hospital.service.criteria.AntecedenteClinicoCriteria;
import com.mycompany.hospital.service.dto.AntecedenteClinicoDTO;
import com.mycompany.hospital.service.mapper.AntecedenteClinicoMapper;
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
 * Service for executing complex queries for {@link AntecedenteClinico} entities in the database.
 * The main input is a {@link AntecedenteClinicoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AntecedenteClinicoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AntecedenteClinicoQueryService extends QueryService<AntecedenteClinico> {

    private static final Logger LOG = LoggerFactory.getLogger(AntecedenteClinicoQueryService.class);

    private final AntecedenteClinicoRepository antecedenteClinicoRepository;

    private final AntecedenteClinicoMapper antecedenteClinicoMapper;

    public AntecedenteClinicoQueryService(
        AntecedenteClinicoRepository antecedenteClinicoRepository,
        AntecedenteClinicoMapper antecedenteClinicoMapper
    ) {
        this.antecedenteClinicoRepository = antecedenteClinicoRepository;
        this.antecedenteClinicoMapper = antecedenteClinicoMapper;
    }

    /**
     * Return a {@link Page} of {@link AntecedenteClinicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AntecedenteClinicoDTO> findByCriteria(AntecedenteClinicoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AntecedenteClinico> specification = createSpecification(criteria);
        return antecedenteClinicoRepository.findAll(specification, page).map(antecedenteClinicoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AntecedenteClinicoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AntecedenteClinico> specification = createSpecification(criteria);
        return antecedenteClinicoRepository.count(specification);
    }

    /**
     * Function to convert {@link AntecedenteClinicoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AntecedenteClinico> createSpecification(AntecedenteClinicoCriteria criteria) {
        Specification<AntecedenteClinico> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), AntecedenteClinico_.id),
                buildStringSpecification(criteria.getTitulo(), AntecedenteClinico_.titulo),
                buildStringSpecification(criteria.getDescripcion(), AntecedenteClinico_.descripcion),
                buildRangeSpecification(criteria.getFechaRegistro(), AntecedenteClinico_.fechaRegistro),
                buildSpecification(criteria.getActivo(), AntecedenteClinico_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), AntecedenteClinico_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), AntecedenteClinico_.fechaBaja),
                buildSpecification(criteria.getHistoriaClinicaId(), root ->
                    root.join(AntecedenteClinico_.historiaClinica, JoinType.LEFT).get(HistoriaClinica_.id)
                )
            );
        }
        return specification;
    }
}
