package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaisCriteriaTest {

    @Test
    void newPaisCriteriaHasAllFiltersNullTest() {
        var paisCriteria = new PaisCriteria();
        assertThat(paisCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paisCriteriaFluentMethodsCreatesFiltersTest() {
        var paisCriteria = new PaisCriteria();

        setAllFilters(paisCriteria);

        assertThat(paisCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paisCriteriaCopyCreatesNullFilterTest() {
        var paisCriteria = new PaisCriteria();
        var copy = paisCriteria.copy();

        assertThat(paisCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paisCriteria)
        );
    }

    @Test
    void paisCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paisCriteria = new PaisCriteria();
        setAllFilters(paisCriteria);

        var copy = paisCriteria.copy();

        assertThat(paisCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paisCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paisCriteria = new PaisCriteria();

        assertThat(paisCriteria).hasToString("PaisCriteria{}");
    }

    private static void setAllFilters(PaisCriteria paisCriteria) {
        paisCriteria.id();
        paisCriteria.nombre();
        paisCriteria.codigoIso();
        paisCriteria.fechaAlta();
        paisCriteria.fechaBaja();
        paisCriteria.activo();
        paisCriteria.distinct();
    }

    private static Condition<PaisCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getCodigoIso()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaisCriteria> copyFiltersAre(PaisCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getCodigoIso(), copy.getCodigoIso()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
