package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FactorRhCriteriaTest {

    @Test
    void newFactorRhCriteriaHasAllFiltersNullTest() {
        var factorRhCriteria = new FactorRhCriteria();
        assertThat(factorRhCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void factorRhCriteriaFluentMethodsCreatesFiltersTest() {
        var factorRhCriteria = new FactorRhCriteria();

        setAllFilters(factorRhCriteria);

        assertThat(factorRhCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void factorRhCriteriaCopyCreatesNullFilterTest() {
        var factorRhCriteria = new FactorRhCriteria();
        var copy = factorRhCriteria.copy();

        assertThat(factorRhCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(factorRhCriteria)
        );
    }

    @Test
    void factorRhCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var factorRhCriteria = new FactorRhCriteria();
        setAllFilters(factorRhCriteria);

        var copy = factorRhCriteria.copy();

        assertThat(factorRhCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(factorRhCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var factorRhCriteria = new FactorRhCriteria();

        assertThat(factorRhCriteria).hasToString("FactorRhCriteria{}");
    }

    private static void setAllFilters(FactorRhCriteria factorRhCriteria) {
        factorRhCriteria.id();
        factorRhCriteria.codigo();
        factorRhCriteria.nombre();
        factorRhCriteria.descripcion();
        factorRhCriteria.activo();
        factorRhCriteria.fechaAlta();
        factorRhCriteria.fechaBaja();
        factorRhCriteria.distinct();
    }

    private static Condition<FactorRhCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<FactorRhCriteria> copyFiltersAre(FactorRhCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
