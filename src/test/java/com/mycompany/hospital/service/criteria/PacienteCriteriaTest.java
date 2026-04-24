package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PacienteCriteriaTest {

    @Test
    void newPacienteCriteriaHasAllFiltersNullTest() {
        var pacienteCriteria = new PacienteCriteria();
        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pacienteCriteriaFluentMethodsCreatesFiltersTest() {
        var pacienteCriteria = new PacienteCriteria();

        setAllFilters(pacienteCriteria);

        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pacienteCriteriaCopyCreatesNullFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void pacienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        setAllFilters(pacienteCriteria);

        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pacienteCriteria = new PacienteCriteria();

        assertThat(pacienteCriteria).hasToString("PacienteCriteria{}");
    }

    private static void setAllFilters(PacienteCriteria pacienteCriteria) {
        pacienteCriteria.id();
        pacienteCriteria.numeroHistoriaClinica();
        pacienteCriteria.fechaAlta();
        pacienteCriteria.fechaBaja();
        pacienteCriteria.activo();
        pacienteCriteria.personaId();
        pacienteCriteria.obraSocialId();
        pacienteCriteria.grupoSanguineoId();
        pacienteCriteria.factorRhId();
        pacienteCriteria.historiaClinicaId();
        pacienteCriteria.distinct();
    }

    private static Condition<PacienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroHistoriaClinica()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getPersonaId()) &&
                condition.apply(criteria.getObraSocialId()) &&
                condition.apply(criteria.getGrupoSanguineoId()) &&
                condition.apply(criteria.getFactorRhId()) &&
                condition.apply(criteria.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PacienteCriteria> copyFiltersAre(PacienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroHistoriaClinica(), copy.getNumeroHistoriaClinica()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getPersonaId(), copy.getPersonaId()) &&
                condition.apply(criteria.getObraSocialId(), copy.getObraSocialId()) &&
                condition.apply(criteria.getGrupoSanguineoId(), copy.getGrupoSanguineoId()) &&
                condition.apply(criteria.getFactorRhId(), copy.getFactorRhId()) &&
                condition.apply(criteria.getHistoriaClinicaId(), copy.getHistoriaClinicaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
