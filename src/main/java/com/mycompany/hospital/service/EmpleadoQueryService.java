package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.repository.EmpleadoRepository;
import com.mycompany.hospital.service.criteria.EmpleadoCriteria;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.mapper.EmpleadoMapper;
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
 * Service for executing complex queries for {@link Empleado} entities in the database.
 * The main input is a {@link EmpleadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmpleadoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmpleadoQueryService extends QueryService<Empleado> {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoQueryService.class);

    private final EmpleadoRepository empleadoRepository;

    private final EmpleadoMapper empleadoMapper;

    public EmpleadoQueryService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
    }

    /**
     * Return a {@link Page} of {@link EmpleadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmpleadoDTO> findByCriteria(EmpleadoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Empleado> specification = createSpecification(criteria);
        return empleadoRepository.findAll(specification, page).map(empleadoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmpleadoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Empleado> specification = createSpecification(criteria);
        return empleadoRepository.count(specification);
    }

    /**
     * Function to convert {@link EmpleadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Empleado> createSpecification(EmpleadoCriteria criteria) {
        Specification<Empleado> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Empleado_.id),
                buildStringSpecification(criteria.getLegajo(), Empleado_.legajo),
                buildRangeSpecification(criteria.getFechaIngreso(), Empleado_.fechaIngreso),
                buildRangeSpecification(criteria.getFechaBaja(), Empleado_.fechaBaja),
                buildSpecification(criteria.getActivo(), Empleado_.activo),
                buildSpecification(criteria.getPersonaId(), root -> root.join(Empleado_.persona, JoinType.LEFT).get(Persona_.id)),
                buildSpecification(criteria.getTipoEmpleadoId(), root ->
                    root.join(Empleado_.tipoEmpleado, JoinType.LEFT).get(TipoEmpleado_.id)
                ),
                buildSpecification(criteria.getEstadoLaboralId(), root ->
                    root.join(Empleado_.estadoLaboral, JoinType.LEFT).get(EstadoLaboral_.id)
                ),
                buildSpecification(criteria.getCargoId(), root -> root.join(Empleado_.cargo, JoinType.LEFT).get(Cargo_.id)),
                buildSpecification(criteria.getMedicoId(), root -> root.join(Empleado_.medico, JoinType.LEFT).get(Medico_.id)),
                buildSpecification(criteria.getEnfermeroId(), root -> root.join(Empleado_.enfermero, JoinType.LEFT).get(Enfermero_.id))
            );
        }
        return specification;
    }
}
