package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoDocumentoCriteriaTest {

    @Test
    void newTipoDocumentoCriteriaHasAllFiltersNullTest() {
        var tipoDocumentoCriteria = new TipoDocumentoCriteria();
        assertThat(tipoDocumentoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoDocumentoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoDocumentoCriteria = new TipoDocumentoCriteria();

        setAllFilters(tipoDocumentoCriteria);

        assertThat(tipoDocumentoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoDocumentoCriteriaCopyCreatesNullFilterTest() {
        var tipoDocumentoCriteria = new TipoDocumentoCriteria();
        var copy = tipoDocumentoCriteria.copy();

        assertThat(tipoDocumentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDocumentoCriteria)
        );
    }

    @Test
    void tipoDocumentoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoDocumentoCriteria = new TipoDocumentoCriteria();
        setAllFilters(tipoDocumentoCriteria);

        var copy = tipoDocumentoCriteria.copy();

        assertThat(tipoDocumentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDocumentoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoDocumentoCriteria = new TipoDocumentoCriteria();

        assertThat(tipoDocumentoCriteria).hasToString("TipoDocumentoCriteria{}");
    }

    private static void setAllFilters(TipoDocumentoCriteria tipoDocumentoCriteria) {
        tipoDocumentoCriteria.id();
        tipoDocumentoCriteria.codigo();
        tipoDocumentoCriteria.nombre();
        tipoDocumentoCriteria.sigla();
        tipoDocumentoCriteria.descripcion();
        tipoDocumentoCriteria.activo();
        tipoDocumentoCriteria.fechaAlta();
        tipoDocumentoCriteria.fechaBaja();
        tipoDocumentoCriteria.distinct();
    }

    private static Condition<TipoDocumentoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getSigla()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoDocumentoCriteria> copyFiltersAre(
        TipoDocumentoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getSigla(), copy.getSigla()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
