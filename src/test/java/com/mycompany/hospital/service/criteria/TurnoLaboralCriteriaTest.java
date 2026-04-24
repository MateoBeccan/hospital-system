package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TurnoLaboralCriteriaTest {

    @Test
    void newTurnoLaboralCriteriaHasAllFiltersNullTest() {
        var turnoLaboralCriteria = new TurnoLaboralCriteria();
        assertThat(turnoLaboralCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void turnoLaboralCriteriaFluentMethodsCreatesFiltersTest() {
        var turnoLaboralCriteria = new TurnoLaboralCriteria();

        setAllFilters(turnoLaboralCriteria);

        assertThat(turnoLaboralCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void turnoLaboralCriteriaCopyCreatesNullFilterTest() {
        var turnoLaboralCriteria = new TurnoLaboralCriteria();
        var copy = turnoLaboralCriteria.copy();

        assertThat(turnoLaboralCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoLaboralCriteria)
        );
    }

    @Test
    void turnoLaboralCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var turnoLaboralCriteria = new TurnoLaboralCriteria();
        setAllFilters(turnoLaboralCriteria);

        var copy = turnoLaboralCriteria.copy();

        assertThat(turnoLaboralCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoLaboralCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var turnoLaboralCriteria = new TurnoLaboralCriteria();

        assertThat(turnoLaboralCriteria).hasToString("TurnoLaboralCriteria{}");
    }

    private static void setAllFilters(TurnoLaboralCriteria turnoLaboralCriteria) {
        turnoLaboralCriteria.id();
        turnoLaboralCriteria.codigo();
        turnoLaboralCriteria.nombre();
        turnoLaboralCriteria.horaInicio();
        turnoLaboralCriteria.horaFin();
        turnoLaboralCriteria.descripcion();
        turnoLaboralCriteria.activo();
        turnoLaboralCriteria.fechaAlta();
        turnoLaboralCriteria.fechaBaja();
        turnoLaboralCriteria.distinct();
    }

    private static Condition<TurnoLaboralCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getHoraInicio()) &&
                condition.apply(criteria.getHoraFin()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TurnoLaboralCriteria> copyFiltersAre(
        TurnoLaboralCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getHoraInicio(), copy.getHoraInicio()) &&
                condition.apply(criteria.getHoraFin(), copy.getHoraFin()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
