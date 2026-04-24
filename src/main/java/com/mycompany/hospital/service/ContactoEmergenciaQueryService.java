package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.ContactoEmergencia;
import com.mycompany.hospital.repository.ContactoEmergenciaRepository;
import com.mycompany.hospital.service.criteria.ContactoEmergenciaCriteria;
import com.mycompany.hospital.service.dto.ContactoEmergenciaDTO;
import com.mycompany.hospital.service.mapper.ContactoEmergenciaMapper;
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
 * Service for executing complex queries for {@link ContactoEmergencia} entities in the database.
 * The main input is a {@link ContactoEmergenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContactoEmergenciaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactoEmergenciaQueryService extends QueryService<ContactoEmergencia> {

    private static final Logger LOG = LoggerFactory.getLogger(ContactoEmergenciaQueryService.class);

    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    private final ContactoEmergenciaMapper contactoEmergenciaMapper;

    public ContactoEmergenciaQueryService(
        ContactoEmergenciaRepository contactoEmergenciaRepository,
        ContactoEmergenciaMapper contactoEmergenciaMapper
    ) {
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
        this.contactoEmergenciaMapper = contactoEmergenciaMapper;
    }

    /**
     * Return a {@link Page} of {@link ContactoEmergenciaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactoEmergenciaDTO> findByCriteria(ContactoEmergenciaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactoEmergencia> specification = createSpecification(criteria);
        return contactoEmergenciaRepository.findAll(specification, page).map(contactoEmergenciaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactoEmergenciaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContactoEmergencia> specification = createSpecification(criteria);
        return contactoEmergenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactoEmergenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactoEmergencia> createSpecification(ContactoEmergenciaCriteria criteria) {
        Specification<ContactoEmergencia> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), ContactoEmergencia_.id),
                buildStringSpecification(criteria.getNombre(), ContactoEmergencia_.nombre),
                buildStringSpecification(criteria.getTelefono(), ContactoEmergencia_.telefono),
                buildStringSpecification(criteria.getParentesco(), ContactoEmergencia_.parentesco),
                buildStringSpecification(criteria.getObservaciones(), ContactoEmergencia_.observaciones),
                buildRangeSpecification(criteria.getPrioridad(), ContactoEmergencia_.prioridad),
                buildSpecification(criteria.getActivo(), ContactoEmergencia_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), ContactoEmergencia_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), ContactoEmergencia_.fechaBaja),
                buildSpecification(criteria.getPersonaId(), root -> root.join(ContactoEmergencia_.persona, JoinType.LEFT).get(Persona_.id))
            );
        }
        return specification;
    }
}
