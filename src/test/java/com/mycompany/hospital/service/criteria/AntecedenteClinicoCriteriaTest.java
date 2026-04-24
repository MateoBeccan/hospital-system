package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AntecedenteClinicoCriteriaTest {

    @Test
    void newAntecedenteClinicoCriteriaHasAllFiltersNullTest() {
        var antecedenteClinicoCriteria = new AntecedenteClinicoCriteria();
        assertThat(antecedenteClinicoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void antecedenteClinicoCriteriaFluentMethodsCreatesFiltersTest() {
        var antecedenteClinicoCriteria = new AntecedenteClinicoCriteria();

        setAllFilters(antecedenteClinicoCriteria);

        assertThat(antecedenteClinicoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void antecedenteClinicoCriteriaCopyCreatesNullFilterTest() {
        var antecedenteClinicoCriteria = new AntecedenteClinicoCriteria();
        var copy = antecedenteClinicoCriteria.copy();

        assertThat(antecedenteClinicoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(antecedenteClinicoCriteria)
        );
    }

    @Test
    void antecedenteClinicoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var antecedenteClinicoCriteria = new AntecedenteClinicoCriteria();
        setAllFilters(antecedenteClinicoCriteria);

        var copy = antecedenteClinicoCriteria.copy();

        assertThat(antecedenteClinicoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(antecedenteClinicoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var antecedenteClinicoCriteria = new AntecedenteClinicoCriteria();

        assertThat(antecedenteClinicoCriteria).hasToString("AntecedenteClinicoCriteria{}");
    }

    private static void setAllFilters(AntecedenteClinicoCriteria antecedenteClinicoCriteria) {
        antecedenteClinicoCriteria.id();
        antecedenteClinicoCriteria.titulo();
        antecedenteClinicoCriteria.descripcion();
        antecedenteClinicoCriteria.fechaRegistro();
        antecedenteClinicoCriteria.activo();
        antecedenteClinicoCriteria.fechaAlta();
        antecedenteClinicoCriteria.fechaBaja();
        antecedenteClinicoCriteria.historiaClinicaId();
        antecedenteClinicoCriteria.distinct();
    }

    private static Condition<AntecedenteClinicoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitulo()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFechaRegistro()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AntecedenteClinicoCriteria> copyFiltersAre(
        AntecedenteClinicoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitulo(), copy.getTitulo()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFechaRegistro(), copy.getFechaRegistro()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getHistoriaClinicaId(), copy.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
