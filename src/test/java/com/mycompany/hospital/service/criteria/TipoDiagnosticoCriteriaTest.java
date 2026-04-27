package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoDiagnosticoCriteriaTest {

    @Test
    void newTipoDiagnosticoCriteriaHasAllFiltersNullTest() {
        var tipoDiagnosticoCriteria = new TipoDiagnosticoCriteria();
        assertThat(tipoDiagnosticoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoDiagnosticoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoDiagnosticoCriteria = new TipoDiagnosticoCriteria();

        setAllFilters(tipoDiagnosticoCriteria);

        assertThat(tipoDiagnosticoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoDiagnosticoCriteriaCopyCreatesNullFilterTest() {
        var tipoDiagnosticoCriteria = new TipoDiagnosticoCriteria();
        var copy = tipoDiagnosticoCriteria.copy();

        assertThat(tipoDiagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDiagnosticoCriteria)
        );
    }

    @Test
    void tipoDiagnosticoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoDiagnosticoCriteria = new TipoDiagnosticoCriteria();
        setAllFilters(tipoDiagnosticoCriteria);

        var copy = tipoDiagnosticoCriteria.copy();

        assertThat(tipoDiagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDiagnosticoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoDiagnosticoCriteria = new TipoDiagnosticoCriteria();

        assertThat(tipoDiagnosticoCriteria).hasToString("TipoDiagnosticoCriteria{}");
    }

    private static void setAllFilters(TipoDiagnosticoCriteria tipoDiagnosticoCriteria) {
        tipoDiagnosticoCriteria.id();
        tipoDiagnosticoCriteria.codigo();
        tipoDiagnosticoCriteria.nombre();
        tipoDiagnosticoCriteria.descripcion();
        tipoDiagnosticoCriteria.activo();
        tipoDiagnosticoCriteria.fechaAlta();
        tipoDiagnosticoCriteria.fechaBaja();
        tipoDiagnosticoCriteria.distinct();
    }

    private static Condition<TipoDiagnosticoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<TipoDiagnosticoCriteria> copyFiltersAre(
        TipoDiagnosticoCriteria copy,
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
