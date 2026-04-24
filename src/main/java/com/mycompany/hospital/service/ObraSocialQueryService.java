package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.repository.ObraSocialRepository;
import com.mycompany.hospital.service.criteria.ObraSocialCriteria;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
import com.mycompany.hospital.service.mapper.ObraSocialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ObraSocial} entities in the database.
 * The main input is a {@link ObraSocialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ObraSocialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ObraSocialQueryService extends QueryService<ObraSocial> {

    private static final Logger LOG = LoggerFactory.getLogger(ObraSocialQueryService.class);

    private final ObraSocialRepository obraSocialRepository;

    private final ObraSocialMapper obraSocialMapper;

    public ObraSocialQueryService(ObraSocialRepository obraSocialRepository, ObraSocialMapper obraSocialMapper) {
        this.obraSocialRepository = obraSocialRepository;
        this.obraSocialMapper = obraSocialMapper;
    }

    /**
     * Return a {@link Page} of {@link ObraSocialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ObraSocialDTO> findByCriteria(ObraSocialCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ObraSocial> specification = createSpecification(criteria);
        return obraSocialRepository.findAll(specification, page).map(obraSocialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ObraSocialCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ObraSocial> specification = createSpecification(criteria);
        return obraSocialRepository.count(specification);
    }

    /**
     * Function to convert {@link ObraSocialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ObraSocial> createSpecification(ObraSocialCriteria criteria) {
        Specification<ObraSocial> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), ObraSocial_.id),
                buildStringSpecification(criteria.getCodigo(), ObraSocial_.codigo),
                buildStringSpecification(criteria.getNombre(), ObraSocial_.nombre),
                buildStringSpecification(criteria.getTelefono(), ObraSocial_.telefono),
                buildStringSpecification(criteria.getEmail(), ObraSocial_.email),
                buildStringSpecification(criteria.getDireccion(), ObraSocial_.direccion),
                buildSpecification(criteria.getActivo(), ObraSocial_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), ObraSocial_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), ObraSocial_.fechaBaja)
            );
        }
        return specification;
    }
}
