package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.repository.TurnoRepository;
import com.mycompany.hospital.service.criteria.TurnoCriteria;
import com.mycompany.hospital.service.dto.TurnoDTO;
import com.mycompany.hospital.service.mapper.TurnoMapper;
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
 * Service for executing complex queries for {@link Turno} entities in the database.
 * The main input is a {@link TurnoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TurnoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TurnoQueryService extends QueryService<Turno> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoQueryService.class);

    private final TurnoRepository turnoRepository;

    private final TurnoMapper turnoMapper;

    public TurnoQueryService(TurnoRepository turnoRepository, TurnoMapper turnoMapper) {
        this.turnoRepository = turnoRepository;
        this.turnoMapper = turnoMapper;
    }

    /**
     * Return a {@link Page} of {@link TurnoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TurnoDTO> findByCriteria(TurnoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Turno> specification = createSpecification(criteria);
        return turnoRepository.findAll(specification, page).map(turnoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TurnoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Turno> specification = createSpecification(criteria);
        return turnoRepository.count(specification);
    }

    /**
     * Function to convert {@link TurnoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Turno> createSpecification(TurnoCriteria criteria) {
        Specification<Turno> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Turno_.id),
                buildStringSpecification(criteria.getCodigo(), Turno_.codigo),
                buildRangeSpecification(criteria.getFechaHora(), Turno_.fechaHora),
                buildRangeSpecification(criteria.getDuracionMinutos(), Turno_.duracionMinutos),
                buildStringSpecification(criteria.getMotivoConsulta(), Turno_.motivoConsulta),
                buildRangeSpecification(criteria.getFechaCreacion(), Turno_.fechaCreacion),
                buildSpecification(criteria.getActivo(), Turno_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), Turno_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Turno_.fechaBaja),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Turno_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getMedicoId(), root -> root.join(Turno_.medico, JoinType.LEFT).get(Medico_.id)),
                buildSpecification(criteria.getEspecialidadId(), root ->
                    root.join(Turno_.especialidad, JoinType.LEFT).get(Especialidad_.id)
                ),
                buildSpecification(criteria.getEstadoTurnoId(), root -> root.join(Turno_.estadoTurno, JoinType.LEFT).get(EstadoTurno_.id)),
                buildSpecification(criteria.getCanalSolicitudId(), root ->
                    root.join(Turno_.canalSolicitud, JoinType.LEFT).get(CanalSolicitud_.id)
                ),
                buildSpecification(criteria.getConsultaId(), root -> root.join(Turno_.consulta, JoinType.LEFT).get(Consulta_.id))
            );
        }
        return specification;
    }
}
