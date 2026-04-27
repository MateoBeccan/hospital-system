package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EstadoTurnoCriteriaTest {

    @Test
    void newEstadoTurnoCriteriaHasAllFiltersNullTest() {
        var estadoTurnoCriteria = new EstadoTurnoCriteria();
        assertThat(estadoTurnoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void estadoTurnoCriteriaFluentMethodsCreatesFiltersTest() {
        var estadoTurnoCriteria = new EstadoTurnoCriteria();

        setAllFilters(estadoTurnoCriteria);

        assertThat(estadoTurnoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void estadoTurnoCriteriaCopyCreatesNullFilterTest() {
        var estadoTurnoCriteria = new EstadoTurnoCriteria();
        var copy = estadoTurnoCriteria.copy();

        assertThat(estadoTurnoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoTurnoCriteria)
        );
    }

    @Test
    void estadoTurnoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var estadoTurnoCriteria = new EstadoTurnoCriteria();
        setAllFilters(estadoTurnoCriteria);

        var copy = estadoTurnoCriteria.copy();

        assertThat(estadoTurnoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoTurnoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var estadoTurnoCriteria = new EstadoTurnoCriteria();

        assertThat(estadoTurnoCriteria).hasToString("EstadoTurnoCriteria{}");
    }

    private static void setAllFilters(EstadoTurnoCriteria estadoTurnoCriteria) {
        estadoTurnoCriteria.id();
        estadoTurnoCriteria.codigo();
        estadoTurnoCriteria.nombre();
        estadoTurnoCriteria.descripcion();
        estadoTurnoCriteria.activo();
        estadoTurnoCriteria.fechaAlta();
        estadoTurnoCriteria.fechaBaja();
        estadoTurnoCriteria.distinct();
    }

    private static Condition<EstadoTurnoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EstadoTurnoCriteria> copyFiltersAre(EstadoTurnoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
