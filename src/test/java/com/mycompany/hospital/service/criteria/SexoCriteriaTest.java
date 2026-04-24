package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SexoCriteriaTest {

    @Test
    void newSexoCriteriaHasAllFiltersNullTest() {
        var sexoCriteria = new SexoCriteria();
        assertThat(sexoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void sexoCriteriaFluentMethodsCreatesFiltersTest() {
        var sexoCriteria = new SexoCriteria();

        setAllFilters(sexoCriteria);

        assertThat(sexoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void sexoCriteriaCopyCreatesNullFilterTest() {
        var sexoCriteria = new SexoCriteria();
        var copy = sexoCriteria.copy();

        assertThat(sexoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(sexoCriteria)
        );
    }

    @Test
    void sexoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sexoCriteria = new SexoCriteria();
        setAllFilters(sexoCriteria);

        var copy = sexoCriteria.copy();

        assertThat(sexoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(sexoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sexoCriteria = new SexoCriteria();

        assertThat(sexoCriteria).hasToString("SexoCriteria{}");
    }

    private static void setAllFilters(SexoCriteria sexoCriteria) {
        sexoCriteria.id();
        sexoCriteria.codigo();
        sexoCriteria.nombre();
        sexoCriteria.descripcion();
        sexoCriteria.fechaAlta();
        sexoCriteria.fechaBaja();
        sexoCriteria.activo();
        sexoCriteria.distinct();
    }

    private static Condition<SexoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<SexoCriteria> copyFiltersAre(SexoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
