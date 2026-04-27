package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EstadoTratamientoCriteriaTest {

    @Test
    void newEstadoTratamientoCriteriaHasAllFiltersNullTest() {
        var estadoTratamientoCriteria = new EstadoTratamientoCriteria();
        assertThat(estadoTratamientoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void estadoTratamientoCriteriaFluentMethodsCreatesFiltersTest() {
        var estadoTratamientoCriteria = new EstadoTratamientoCriteria();

        setAllFilters(estadoTratamientoCriteria);

        assertThat(estadoTratamientoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void estadoTratamientoCriteriaCopyCreatesNullFilterTest() {
        var estadoTratamientoCriteria = new EstadoTratamientoCriteria();
        var copy = estadoTratamientoCriteria.copy();

        assertThat(estadoTratamientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoTratamientoCriteria)
        );
    }

    @Test
    void estadoTratamientoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var estadoTratamientoCriteria = new EstadoTratamientoCriteria();
        setAllFilters(estadoTratamientoCriteria);

        var copy = estadoTratamientoCriteria.copy();

        assertThat(estadoTratamientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoTratamientoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var estadoTratamientoCriteria = new EstadoTratamientoCriteria();

        assertThat(estadoTratamientoCriteria).hasToString("EstadoTratamientoCriteria{}");
    }

    private static void setAllFilters(EstadoTratamientoCriteria estadoTratamientoCriteria) {
        estadoTratamientoCriteria.id();
        estadoTratamientoCriteria.codigo();
        estadoTratamientoCriteria.nombre();
        estadoTratamientoCriteria.descripcion();
        estadoTratamientoCriteria.activo();
        estadoTratamientoCriteria.fechaAlta();
        estadoTratamientoCriteria.fechaBaja();
        estadoTratamientoCriteria.distinct();
    }

    private static Condition<EstadoTratamientoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EstadoTratamientoCriteria> copyFiltersAre(
        EstadoTratamientoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
