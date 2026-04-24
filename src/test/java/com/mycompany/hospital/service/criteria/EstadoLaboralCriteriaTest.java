package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EstadoLaboralCriteriaTest {

    @Test
    void newEstadoLaboralCriteriaHasAllFiltersNullTest() {
        var estadoLaboralCriteria = new EstadoLaboralCriteria();
        assertThat(estadoLaboralCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void estadoLaboralCriteriaFluentMethodsCreatesFiltersTest() {
        var estadoLaboralCriteria = new EstadoLaboralCriteria();

        setAllFilters(estadoLaboralCriteria);

        assertThat(estadoLaboralCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void estadoLaboralCriteriaCopyCreatesNullFilterTest() {
        var estadoLaboralCriteria = new EstadoLaboralCriteria();
        var copy = estadoLaboralCriteria.copy();

        assertThat(estadoLaboralCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoLaboralCriteria)
        );
    }

    @Test
    void estadoLaboralCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var estadoLaboralCriteria = new EstadoLaboralCriteria();
        setAllFilters(estadoLaboralCriteria);

        var copy = estadoLaboralCriteria.copy();

        assertThat(estadoLaboralCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoLaboralCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var estadoLaboralCriteria = new EstadoLaboralCriteria();

        assertThat(estadoLaboralCriteria).hasToString("EstadoLaboralCriteria{}");
    }

    private static void setAllFilters(EstadoLaboralCriteria estadoLaboralCriteria) {
        estadoLaboralCriteria.id();
        estadoLaboralCriteria.codigo();
        estadoLaboralCriteria.nombre();
        estadoLaboralCriteria.descripcion();
        estadoLaboralCriteria.fechaAlta();
        estadoLaboralCriteria.fechaBaja();
        estadoLaboralCriteria.activo();
        estadoLaboralCriteria.distinct();
    }

    private static Condition<EstadoLaboralCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EstadoLaboralCriteria> copyFiltersAre(
        EstadoLaboralCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
