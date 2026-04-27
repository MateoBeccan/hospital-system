package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConsultaCriteriaTest {

    @Test
    void newConsultaCriteriaHasAllFiltersNullTest() {
        var consultaCriteria = new ConsultaCriteria();
        assertThat(consultaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void consultaCriteriaFluentMethodsCreatesFiltersTest() {
        var consultaCriteria = new ConsultaCriteria();

        setAllFilters(consultaCriteria);

        assertThat(consultaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void consultaCriteriaCopyCreatesNullFilterTest() {
        var consultaCriteria = new ConsultaCriteria();
        var copy = consultaCriteria.copy();

        assertThat(consultaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(consultaCriteria)
        );
    }

    @Test
    void consultaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var consultaCriteria = new ConsultaCriteria();
        setAllFilters(consultaCriteria);

        var copy = consultaCriteria.copy();

        assertThat(consultaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(consultaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var consultaCriteria = new ConsultaCriteria();

        assertThat(consultaCriteria).hasToString("ConsultaCriteria{}");
    }

    private static void setAllFilters(ConsultaCriteria consultaCriteria) {
        consultaCriteria.id();
        consultaCriteria.codigo();
        consultaCriteria.fechaHoraInicio();
        consultaCriteria.fechaHoraFin();
        consultaCriteria.motivoConsulta();
        consultaCriteria.activa();
        consultaCriteria.fechaAlta();
        consultaCriteria.fechaBaja();
        consultaCriteria.turnoId();
        consultaCriteria.pacienteId();
        consultaCriteria.medicoId();
        consultaCriteria.historiaClinicaId();
        consultaCriteria.distinct();
    }

    private static Condition<ConsultaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getFechaHoraInicio()) &&
                condition.apply(criteria.getFechaHoraFin()) &&
                condition.apply(criteria.getMotivoConsulta()) &&
                condition.apply(criteria.getActiva()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getTurnoId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getMedicoId()) &&
                condition.apply(criteria.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConsultaCriteria> copyFiltersAre(ConsultaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getFechaHoraInicio(), copy.getFechaHoraInicio()) &&
                condition.apply(criteria.getFechaHoraFin(), copy.getFechaHoraFin()) &&
                condition.apply(criteria.getMotivoConsulta(), copy.getMotivoConsulta()) &&
                condition.apply(criteria.getActiva(), copy.getActiva()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getTurnoId(), copy.getTurnoId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getMedicoId(), copy.getMedicoId()) &&
                condition.apply(criteria.getHistoriaClinicaId(), copy.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
