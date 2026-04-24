package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CiudadCriteriaTest {

    @Test
    void newCiudadCriteriaHasAllFiltersNullTest() {
        var ciudadCriteria = new CiudadCriteria();
        assertThat(ciudadCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ciudadCriteriaFluentMethodsCreatesFiltersTest() {
        var ciudadCriteria = new CiudadCriteria();

        setAllFilters(ciudadCriteria);

        assertThat(ciudadCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ciudadCriteriaCopyCreatesNullFilterTest() {
        var ciudadCriteria = new CiudadCriteria();
        var copy = ciudadCriteria.copy();

        assertThat(ciudadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ciudadCriteria)
        );
    }

    @Test
    void ciudadCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ciudadCriteria = new CiudadCriteria();
        setAllFilters(ciudadCriteria);

        var copy = ciudadCriteria.copy();

        assertThat(ciudadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ciudadCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ciudadCriteria = new CiudadCriteria();

        assertThat(ciudadCriteria).hasToString("CiudadCriteria{}");
    }

    private static void setAllFilters(CiudadCriteria ciudadCriteria) {
        ciudadCriteria.id();
        ciudadCriteria.nombre();
        ciudadCriteria.codigo();
        ciudadCriteria.codigoPostal();
        ciudadCriteria.fechaAlta();
        ciudadCriteria.fechaBaja();
        ciudadCriteria.activo();
        ciudadCriteria.provinciaId();
        ciudadCriteria.distinct();
    }

    private static Condition<CiudadCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getCodigoPostal()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getProvinciaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CiudadCriteria> copyFiltersAre(CiudadCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getCodigoPostal(), copy.getCodigoPostal()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getProvinciaId(), copy.getProvinciaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
