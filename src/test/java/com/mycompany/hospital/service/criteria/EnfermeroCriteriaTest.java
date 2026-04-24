package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EnfermeroCriteriaTest {

    @Test
    void newEnfermeroCriteriaHasAllFiltersNullTest() {
        var enfermeroCriteria = new EnfermeroCriteria();
        assertThat(enfermeroCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void enfermeroCriteriaFluentMethodsCreatesFiltersTest() {
        var enfermeroCriteria = new EnfermeroCriteria();

        setAllFilters(enfermeroCriteria);

        assertThat(enfermeroCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void enfermeroCriteriaCopyCreatesNullFilterTest() {
        var enfermeroCriteria = new EnfermeroCriteria();
        var copy = enfermeroCriteria.copy();

        assertThat(enfermeroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(enfermeroCriteria)
        );
    }

    @Test
    void enfermeroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var enfermeroCriteria = new EnfermeroCriteria();
        setAllFilters(enfermeroCriteria);

        var copy = enfermeroCriteria.copy();

        assertThat(enfermeroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(enfermeroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var enfermeroCriteria = new EnfermeroCriteria();

        assertThat(enfermeroCriteria).hasToString("EnfermeroCriteria{}");
    }

    private static void setAllFilters(EnfermeroCriteria enfermeroCriteria) {
        enfermeroCriteria.id();
        enfermeroCriteria.matricula();
        enfermeroCriteria.fechaMatriculacion();
        enfermeroCriteria.activo();
        enfermeroCriteria.fechaAlta();
        enfermeroCriteria.fechaBaja();
        enfermeroCriteria.empleadoId();
        enfermeroCriteria.turnoLaboralId();
        enfermeroCriteria.distinct();
    }

    private static Condition<EnfermeroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMatricula()) &&
                condition.apply(criteria.getFechaMatriculacion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getEmpleadoId()) &&
                condition.apply(criteria.getTurnoLaboralId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EnfermeroCriteria> copyFiltersAre(EnfermeroCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMatricula(), copy.getMatricula()) &&
                condition.apply(criteria.getFechaMatriculacion(), copy.getFechaMatriculacion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getEmpleadoId(), copy.getEmpleadoId()) &&
                condition.apply(criteria.getTurnoLaboralId(), copy.getTurnoLaboralId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
