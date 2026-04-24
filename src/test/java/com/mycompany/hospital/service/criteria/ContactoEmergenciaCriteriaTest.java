package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContactoEmergenciaCriteriaTest {

    @Test
    void newContactoEmergenciaCriteriaHasAllFiltersNullTest() {
        var contactoEmergenciaCriteria = new ContactoEmergenciaCriteria();
        assertThat(contactoEmergenciaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contactoEmergenciaCriteriaFluentMethodsCreatesFiltersTest() {
        var contactoEmergenciaCriteria = new ContactoEmergenciaCriteria();

        setAllFilters(contactoEmergenciaCriteria);

        assertThat(contactoEmergenciaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contactoEmergenciaCriteriaCopyCreatesNullFilterTest() {
        var contactoEmergenciaCriteria = new ContactoEmergenciaCriteria();
        var copy = contactoEmergenciaCriteria.copy();

        assertThat(contactoEmergenciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contactoEmergenciaCriteria)
        );
    }

    @Test
    void contactoEmergenciaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contactoEmergenciaCriteria = new ContactoEmergenciaCriteria();
        setAllFilters(contactoEmergenciaCriteria);

        var copy = contactoEmergenciaCriteria.copy();

        assertThat(contactoEmergenciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contactoEmergenciaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contactoEmergenciaCriteria = new ContactoEmergenciaCriteria();

        assertThat(contactoEmergenciaCriteria).hasToString("ContactoEmergenciaCriteria{}");
    }

    private static void setAllFilters(ContactoEmergenciaCriteria contactoEmergenciaCriteria) {
        contactoEmergenciaCriteria.id();
        contactoEmergenciaCriteria.nombre();
        contactoEmergenciaCriteria.telefono();
        contactoEmergenciaCriteria.parentesco();
        contactoEmergenciaCriteria.observaciones();
        contactoEmergenciaCriteria.prioridad();
        contactoEmergenciaCriteria.activo();
        contactoEmergenciaCriteria.fechaAlta();
        contactoEmergenciaCriteria.fechaBaja();
        contactoEmergenciaCriteria.personaId();
        contactoEmergenciaCriteria.distinct();
    }

    private static Condition<ContactoEmergenciaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getParentesco()) &&
                condition.apply(criteria.getObservaciones()) &&
                condition.apply(criteria.getPrioridad()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getPersonaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContactoEmergenciaCriteria> copyFiltersAre(
        ContactoEmergenciaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getParentesco(), copy.getParentesco()) &&
                condition.apply(criteria.getObservaciones(), copy.getObservaciones()) &&
                condition.apply(criteria.getPrioridad(), copy.getPrioridad()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getPersonaId(), copy.getPersonaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
