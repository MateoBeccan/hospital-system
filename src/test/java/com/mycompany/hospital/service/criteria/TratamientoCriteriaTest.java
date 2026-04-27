package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TratamientoCriteriaTest {

    @Test
    void newTratamientoCriteriaHasAllFiltersNullTest() {
        var tratamientoCriteria = new TratamientoCriteria();
        assertThat(tratamientoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tratamientoCriteriaFluentMethodsCreatesFiltersTest() {
        var tratamientoCriteria = new TratamientoCriteria();

        setAllFilters(tratamientoCriteria);

        assertThat(tratamientoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tratamientoCriteriaCopyCreatesNullFilterTest() {
        var tratamientoCriteria = new TratamientoCriteria();
        var copy = tratamientoCriteria.copy();

        assertThat(tratamientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tratamientoCriteria)
        );
    }

    @Test
    void tratamientoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tratamientoCriteria = new TratamientoCriteria();
        setAllFilters(tratamientoCriteria);

        var copy = tratamientoCriteria.copy();

        assertThat(tratamientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tratamientoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tratamientoCriteria = new TratamientoCriteria();

        assertThat(tratamientoCriteria).hasToString("TratamientoCriteria{}");
    }

    private static void setAllFilters(TratamientoCriteria tratamientoCriteria) {
        tratamientoCriteria.id();
        tratamientoCriteria.codigo();
        tratamientoCriteria.descripcion();
        tratamientoCriteria.fechaInicio();
        tratamientoCriteria.fechaFin();
        tratamientoCriteria.fechaProximaRevision();
        tratamientoCriteria.activo();
        tratamientoCriteria.fechaAlta();
        tratamientoCriteria.fechaBaja();
        tratamientoCriteria.diagnosticoId();
        tratamientoCriteria.estadoTratamientoId();
        tratamientoCriteria.distinct();
    }

    private static Condition<TratamientoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin()) &&
                condition.apply(criteria.getFechaProximaRevision()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getDiagnosticoId()) &&
                condition.apply(criteria.getEstadoTratamientoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TratamientoCriteria> copyFiltersAre(TratamientoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFechaInicio(), copy.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin(), copy.getFechaFin()) &&
                condition.apply(criteria.getFechaProximaRevision(), copy.getFechaProximaRevision()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getDiagnosticoId(), copy.getDiagnosticoId()) &&
                condition.apply(criteria.getEstadoTratamientoId(), copy.getEstadoTratamientoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
