package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoEmpleadoCriteriaTest {

    @Test
    void newTipoEmpleadoCriteriaHasAllFiltersNullTest() {
        var tipoEmpleadoCriteria = new TipoEmpleadoCriteria();
        assertThat(tipoEmpleadoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoEmpleadoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoEmpleadoCriteria = new TipoEmpleadoCriteria();

        setAllFilters(tipoEmpleadoCriteria);

        assertThat(tipoEmpleadoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoEmpleadoCriteriaCopyCreatesNullFilterTest() {
        var tipoEmpleadoCriteria = new TipoEmpleadoCriteria();
        var copy = tipoEmpleadoCriteria.copy();

        assertThat(tipoEmpleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoEmpleadoCriteria)
        );
    }

    @Test
    void tipoEmpleadoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoEmpleadoCriteria = new TipoEmpleadoCriteria();
        setAllFilters(tipoEmpleadoCriteria);

        var copy = tipoEmpleadoCriteria.copy();

        assertThat(tipoEmpleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoEmpleadoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoEmpleadoCriteria = new TipoEmpleadoCriteria();

        assertThat(tipoEmpleadoCriteria).hasToString("TipoEmpleadoCriteria{}");
    }

    private static void setAllFilters(TipoEmpleadoCriteria tipoEmpleadoCriteria) {
        tipoEmpleadoCriteria.id();
        tipoEmpleadoCriteria.codigo();
        tipoEmpleadoCriteria.nombre();
        tipoEmpleadoCriteria.descripcion();
        tipoEmpleadoCriteria.fechaAlta();
        tipoEmpleadoCriteria.fechaBaja();
        tipoEmpleadoCriteria.activo();
        tipoEmpleadoCriteria.distinct();
    }

    private static Condition<TipoEmpleadoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<TipoEmpleadoCriteria> copyFiltersAre(
        TipoEmpleadoCriteria copy,
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
