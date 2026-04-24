package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GrupoSanguineoCriteriaTest {

    @Test
    void newGrupoSanguineoCriteriaHasAllFiltersNullTest() {
        var grupoSanguineoCriteria = new GrupoSanguineoCriteria();
        assertThat(grupoSanguineoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void grupoSanguineoCriteriaFluentMethodsCreatesFiltersTest() {
        var grupoSanguineoCriteria = new GrupoSanguineoCriteria();

        setAllFilters(grupoSanguineoCriteria);

        assertThat(grupoSanguineoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void grupoSanguineoCriteriaCopyCreatesNullFilterTest() {
        var grupoSanguineoCriteria = new GrupoSanguineoCriteria();
        var copy = grupoSanguineoCriteria.copy();

        assertThat(grupoSanguineoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(grupoSanguineoCriteria)
        );
    }

    @Test
    void grupoSanguineoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var grupoSanguineoCriteria = new GrupoSanguineoCriteria();
        setAllFilters(grupoSanguineoCriteria);

        var copy = grupoSanguineoCriteria.copy();

        assertThat(grupoSanguineoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(grupoSanguineoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var grupoSanguineoCriteria = new GrupoSanguineoCriteria();

        assertThat(grupoSanguineoCriteria).hasToString("GrupoSanguineoCriteria{}");
    }

    private static void setAllFilters(GrupoSanguineoCriteria grupoSanguineoCriteria) {
        grupoSanguineoCriteria.id();
        grupoSanguineoCriteria.codigo();
        grupoSanguineoCriteria.nombre();
        grupoSanguineoCriteria.descripcion();
        grupoSanguineoCriteria.activo();
        grupoSanguineoCriteria.fechaAlta();
        grupoSanguineoCriteria.fechaBaja();
        grupoSanguineoCriteria.distinct();
    }

    private static Condition<GrupoSanguineoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<GrupoSanguineoCriteria> copyFiltersAre(
        GrupoSanguineoCriteria copy,
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
