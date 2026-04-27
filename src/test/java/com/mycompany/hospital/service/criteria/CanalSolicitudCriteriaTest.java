package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CanalSolicitudCriteriaTest {

    @Test
    void newCanalSolicitudCriteriaHasAllFiltersNullTest() {
        var canalSolicitudCriteria = new CanalSolicitudCriteria();
        assertThat(canalSolicitudCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void canalSolicitudCriteriaFluentMethodsCreatesFiltersTest() {
        var canalSolicitudCriteria = new CanalSolicitudCriteria();

        setAllFilters(canalSolicitudCriteria);

        assertThat(canalSolicitudCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void canalSolicitudCriteriaCopyCreatesNullFilterTest() {
        var canalSolicitudCriteria = new CanalSolicitudCriteria();
        var copy = canalSolicitudCriteria.copy();

        assertThat(canalSolicitudCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(canalSolicitudCriteria)
        );
    }

    @Test
    void canalSolicitudCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var canalSolicitudCriteria = new CanalSolicitudCriteria();
        setAllFilters(canalSolicitudCriteria);

        var copy = canalSolicitudCriteria.copy();

        assertThat(canalSolicitudCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(canalSolicitudCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var canalSolicitudCriteria = new CanalSolicitudCriteria();

        assertThat(canalSolicitudCriteria).hasToString("CanalSolicitudCriteria{}");
    }

    private static void setAllFilters(CanalSolicitudCriteria canalSolicitudCriteria) {
        canalSolicitudCriteria.id();
        canalSolicitudCriteria.codigo();
        canalSolicitudCriteria.nombre();
        canalSolicitudCriteria.descripcion();
        canalSolicitudCriteria.activo();
        canalSolicitudCriteria.fechaAlta();
        canalSolicitudCriteria.fechaBaja();
        canalSolicitudCriteria.distinct();
    }

    private static Condition<CanalSolicitudCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<CanalSolicitudCriteria> copyFiltersAre(
        CanalSolicitudCriteria copy,
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
