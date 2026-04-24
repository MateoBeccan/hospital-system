package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ObraSocialCriteriaTest {

    @Test
    void newObraSocialCriteriaHasAllFiltersNullTest() {
        var obraSocialCriteria = new ObraSocialCriteria();
        assertThat(obraSocialCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void obraSocialCriteriaFluentMethodsCreatesFiltersTest() {
        var obraSocialCriteria = new ObraSocialCriteria();

        setAllFilters(obraSocialCriteria);

        assertThat(obraSocialCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void obraSocialCriteriaCopyCreatesNullFilterTest() {
        var obraSocialCriteria = new ObraSocialCriteria();
        var copy = obraSocialCriteria.copy();

        assertThat(obraSocialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(obraSocialCriteria)
        );
    }

    @Test
    void obraSocialCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var obraSocialCriteria = new ObraSocialCriteria();
        setAllFilters(obraSocialCriteria);

        var copy = obraSocialCriteria.copy();

        assertThat(obraSocialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(obraSocialCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var obraSocialCriteria = new ObraSocialCriteria();

        assertThat(obraSocialCriteria).hasToString("ObraSocialCriteria{}");
    }

    private static void setAllFilters(ObraSocialCriteria obraSocialCriteria) {
        obraSocialCriteria.id();
        obraSocialCriteria.codigo();
        obraSocialCriteria.nombre();
        obraSocialCriteria.telefono();
        obraSocialCriteria.email();
        obraSocialCriteria.direccion();
        obraSocialCriteria.activo();
        obraSocialCriteria.fechaAlta();
        obraSocialCriteria.fechaBaja();
        obraSocialCriteria.distinct();
    }

    private static Condition<ObraSocialCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDireccion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ObraSocialCriteria> copyFiltersAre(ObraSocialCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDireccion(), copy.getDireccion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
