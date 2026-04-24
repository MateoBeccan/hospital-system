package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.repository.MedicoRepository;
import com.mycompany.hospital.service.criteria.MedicoCriteria;
import com.mycompany.hospital.service.dto.MedicoDTO;
import com.mycompany.hospital.service.mapper.MedicoMapper;
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
 * Service for executing complex queries for {@link Medico} entities in the database.
 * The main input is a {@link MedicoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicoQueryService extends QueryService<Medico> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicoQueryService.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoQueryService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicoDTO> findByCriteria(MedicoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medico> specification = createSpecification(criteria);
        return medicoRepository.findAll(specification, page).map(medicoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medico> specification = createSpecification(criteria);
        return medicoRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medico> createSpecification(MedicoCriteria criteria) {
        Specification<Medico> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Medico_.id),
                buildStringSpecification(criteria.getMatricula(), Medico_.matricula),
                buildRangeSpecification(criteria.getFechaMatriculacion(), Medico_.fechaMatriculacion),
                buildStringSpecification(criteria.getFirmaDigital(), Medico_.firmaDigital),
                buildSpecification(criteria.getAtiendeConsultorio(), Medico_.atiendeConsultorio),
                buildSpecification(criteria.getActivo(), Medico_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), Medico_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Medico_.fechaBaja),
                buildSpecification(criteria.getEmpleadoId(), root -> root.join(Medico_.empleado, JoinType.LEFT).get(Empleado_.id)),
                buildSpecification(criteria.getEspecialidadId(), root ->
                    root.join(Medico_.especialidad, JoinType.LEFT).get(Especialidad_.id)
                )
            );
        }
        return specification;
    }
}
