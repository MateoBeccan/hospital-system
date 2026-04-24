package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoriaClinicaCriteriaTest {

    @Test
    void newHistoriaClinicaCriteriaHasAllFiltersNullTest() {
        var historiaClinicaCriteria = new HistoriaClinicaCriteria();
        assertThat(historiaClinicaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historiaClinicaCriteriaFluentMethodsCreatesFiltersTest() {
        var historiaClinicaCriteria = new HistoriaClinicaCriteria();

        setAllFilters(historiaClinicaCriteria);

        assertThat(historiaClinicaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historiaClinicaCriteriaCopyCreatesNullFilterTest() {
        var historiaClinicaCriteria = new HistoriaClinicaCriteria();
        var copy = historiaClinicaCriteria.copy();

        assertThat(historiaClinicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historiaClinicaCriteria)
        );
    }

    @Test
    void historiaClinicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historiaClinicaCriteria = new HistoriaClinicaCriteria();
        setAllFilters(historiaClinicaCriteria);

        var copy = historiaClinicaCriteria.copy();

        assertThat(historiaClinicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historiaClinicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historiaClinicaCriteria = new HistoriaClinicaCriteria();

        assertThat(historiaClinicaCriteria).hasToString("HistoriaClinicaCriteria{}");
    }

    private static void setAllFilters(HistoriaClinicaCriteria historiaClinicaCriteria) {
        historiaClinicaCriteria.id();
        historiaClinicaCriteria.numero();
        historiaClinicaCriteria.fechaApertura();
        historiaClinicaCriteria.fechaUltimaActualizacion();
        historiaClinicaCriteria.activa();
        historiaClinicaCriteria.fechaCierre();
        historiaClinicaCriteria.motivoCierre();
        historiaClinicaCriteria.pacienteId();
        historiaClinicaCriteria.distinct();
    }

    private static Condition<HistoriaClinicaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getFechaApertura()) &&
                condition.apply(criteria.getFechaUltimaActualizacion()) &&
                condition.apply(criteria.getActiva()) &&
                condition.apply(criteria.getFechaCierre()) &&
                condition.apply(criteria.getMotivoCierre()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoriaClinicaCriteria> copyFiltersAre(
        HistoriaClinicaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getFechaApertura(), copy.getFechaApertura()) &&
                condition.apply(criteria.getFechaUltimaActualizacion(), copy.getFechaUltimaActualizacion()) &&
                condition.apply(criteria.getActiva(), copy.getActiva()) &&
                condition.apply(criteria.getFechaCierre(), copy.getFechaCierre()) &&
                condition.apply(criteria.getMotivoCierre(), copy.getMotivoCierre()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
