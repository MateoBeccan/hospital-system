package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CargoCriteriaTest {

    @Test
    void newCargoCriteriaHasAllFiltersNullTest() {
        var cargoCriteria = new CargoCriteria();
        assertThat(cargoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cargoCriteriaFluentMethodsCreatesFiltersTest() {
        var cargoCriteria = new CargoCriteria();

        setAllFilters(cargoCriteria);

        assertThat(cargoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cargoCriteriaCopyCreatesNullFilterTest() {
        var cargoCriteria = new CargoCriteria();
        var copy = cargoCriteria.copy();

        assertThat(cargoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoCriteria)
        );
    }

    @Test
    void cargoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cargoCriteria = new CargoCriteria();
        setAllFilters(cargoCriteria);

        var copy = cargoCriteria.copy();

        assertThat(cargoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cargoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cargoCriteria = new CargoCriteria();

        assertThat(cargoCriteria).hasToString("CargoCriteria{}");
    }

    private static void setAllFilters(CargoCriteria cargoCriteria) {
        cargoCriteria.id();
        cargoCriteria.codigo();
        cargoCriteria.nombre();
        cargoCriteria.descripcion();
        cargoCriteria.activo();
        cargoCriteria.fechaAlta();
        cargoCriteria.fechaBaja();
        cargoCriteria.distinct();
    }

    private static Condition<CargoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CargoCriteria> copyFiltersAre(CargoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
