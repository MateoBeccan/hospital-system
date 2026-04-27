package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EstadoDiagnosticoCriteriaTest {

    @Test
    void newEstadoDiagnosticoCriteriaHasAllFiltersNullTest() {
        var estadoDiagnosticoCriteria = new EstadoDiagnosticoCriteria();
        assertThat(estadoDiagnosticoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void estadoDiagnosticoCriteriaFluentMethodsCreatesFiltersTest() {
        var estadoDiagnosticoCriteria = new EstadoDiagnosticoCriteria();

        setAllFilters(estadoDiagnosticoCriteria);

        assertThat(estadoDiagnosticoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void estadoDiagnosticoCriteriaCopyCreatesNullFilterTest() {
        var estadoDiagnosticoCriteria = new EstadoDiagnosticoCriteria();
        var copy = estadoDiagnosticoCriteria.copy();

        assertThat(estadoDiagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoDiagnosticoCriteria)
        );
    }

    @Test
    void estadoDiagnosticoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var estadoDiagnosticoCriteria = new EstadoDiagnosticoCriteria();
        setAllFilters(estadoDiagnosticoCriteria);

        var copy = estadoDiagnosticoCriteria.copy();

        assertThat(estadoDiagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(estadoDiagnosticoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var estadoDiagnosticoCriteria = new EstadoDiagnosticoCriteria();

        assertThat(estadoDiagnosticoCriteria).hasToString("EstadoDiagnosticoCriteria{}");
    }

    private static void setAllFilters(EstadoDiagnosticoCriteria estadoDiagnosticoCriteria) {
        estadoDiagnosticoCriteria.id();
        estadoDiagnosticoCriteria.codigo();
        estadoDiagnosticoCriteria.nombre();
        estadoDiagnosticoCriteria.descripcion();
        estadoDiagnosticoCriteria.activo();
        estadoDiagnosticoCriteria.fechaAlta();
        estadoDiagnosticoCriteria.fechaBaja();
        estadoDiagnosticoCriteria.distinct();
    }

    private static Condition<EstadoDiagnosticoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<EstadoDiagnosticoCriteria> copyFiltersAre(
        EstadoDiagnosticoCriteria copy,
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
