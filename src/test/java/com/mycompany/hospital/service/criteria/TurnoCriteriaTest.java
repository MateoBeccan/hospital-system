package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TurnoCriteriaTest {

    @Test
    void newTurnoCriteriaHasAllFiltersNullTest() {
        var turnoCriteria = new TurnoCriteria();
        assertThat(turnoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void turnoCriteriaFluentMethodsCreatesFiltersTest() {
        var turnoCriteria = new TurnoCriteria();

        setAllFilters(turnoCriteria);

        assertThat(turnoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void turnoCriteriaCopyCreatesNullFilterTest() {
        var turnoCriteria = new TurnoCriteria();
        var copy = turnoCriteria.copy();

        assertThat(turnoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoCriteria)
        );
    }

    @Test
    void turnoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var turnoCriteria = new TurnoCriteria();
        setAllFilters(turnoCriteria);

        var copy = turnoCriteria.copy();

        assertThat(turnoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var turnoCriteria = new TurnoCriteria();

        assertThat(turnoCriteria).hasToString("TurnoCriteria{}");
    }

    private static void setAllFilters(TurnoCriteria turnoCriteria) {
        turnoCriteria.id();
        turnoCriteria.codigo();
        turnoCriteria.fechaHora();
        turnoCriteria.duracionMinutos();
        turnoCriteria.motivoConsulta();
        turnoCriteria.fechaCreacion();
        turnoCriteria.activo();
        turnoCriteria.fechaAlta();
        turnoCriteria.fechaBaja();
        turnoCriteria.pacienteId();
        turnoCriteria.medicoId();
        turnoCriteria.especialidadId();
        turnoCriteria.estadoTurnoId();
        turnoCriteria.canalSolicitudId();
        turnoCriteria.consultaId();
        turnoCriteria.distinct();
    }

    private static Condition<TurnoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getFechaHora()) &&
                condition.apply(criteria.getDuracionMinutos()) &&
                condition.apply(criteria.getMotivoConsulta()) &&
                condition.apply(criteria.getFechaCreacion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getMedicoId()) &&
                condition.apply(criteria.getEspecialidadId()) &&
                condition.apply(criteria.getEstadoTurnoId()) &&
                condition.apply(criteria.getCanalSolicitudId()) &&
                condition.apply(criteria.getConsultaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TurnoCriteria> copyFiltersAre(TurnoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getFechaHora(), copy.getFechaHora()) &&
                condition.apply(criteria.getDuracionMinutos(), copy.getDuracionMinutos()) &&
                condition.apply(criteria.getMotivoConsulta(), copy.getMotivoConsulta()) &&
                condition.apply(criteria.getFechaCreacion(), copy.getFechaCreacion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getMedicoId(), copy.getMedicoId()) &&
                condition.apply(criteria.getEspecialidadId(), copy.getEspecialidadId()) &&
                condition.apply(criteria.getEstadoTurnoId(), copy.getEstadoTurnoId()) &&
                condition.apply(criteria.getCanalSolicitudId(), copy.getCanalSolicitudId()) &&
                condition.apply(criteria.getConsultaId(), copy.getConsultaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
