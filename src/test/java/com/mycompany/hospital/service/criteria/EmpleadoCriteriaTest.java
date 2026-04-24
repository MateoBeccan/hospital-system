package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmpleadoCriteriaTest {

    @Test
    void newEmpleadoCriteriaHasAllFiltersNullTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        assertThat(empleadoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void empleadoCriteriaFluentMethodsCreatesFiltersTest() {
        var empleadoCriteria = new EmpleadoCriteria();

        setAllFilters(empleadoCriteria);

        assertThat(empleadoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void empleadoCriteriaCopyCreatesNullFilterTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        var copy = empleadoCriteria.copy();

        assertThat(empleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(empleadoCriteria)
        );
    }

    @Test
    void empleadoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        setAllFilters(empleadoCriteria);

        var copy = empleadoCriteria.copy();

        assertThat(empleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(empleadoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var empleadoCriteria = new EmpleadoCriteria();

        assertThat(empleadoCriteria).hasToString("EmpleadoCriteria{}");
    }

    private static void setAllFilters(EmpleadoCriteria empleadoCriteria) {
        empleadoCriteria.id();
        empleadoCriteria.legajo();
        empleadoCriteria.fechaIngreso();
        empleadoCriteria.fechaBaja();
        empleadoCriteria.activo();
        empleadoCriteria.personaId();
        empleadoCriteria.tipoEmpleadoId();
        empleadoCriteria.estadoLaboralId();
        empleadoCriteria.cargoId();
        empleadoCriteria.medicoId();
        empleadoCriteria.enfermeroId();
        empleadoCriteria.distinct();
    }

    private static Condition<EmpleadoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLegajo()) &&
                condition.apply(criteria.getFechaIngreso()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getPersonaId()) &&
                condition.apply(criteria.getTipoEmpleadoId()) &&
                condition.apply(criteria.getEstadoLaboralId()) &&
                condition.apply(criteria.getCargoId()) &&
                condition.apply(criteria.getMedicoId()) &&
                condition.apply(criteria.getEnfermeroId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmpleadoCriteria> copyFiltersAre(EmpleadoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLegajo(), copy.getLegajo()) &&
                condition.apply(criteria.getFechaIngreso(), copy.getFechaIngreso()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getPersonaId(), copy.getPersonaId()) &&
                condition.apply(criteria.getTipoEmpleadoId(), copy.getTipoEmpleadoId()) &&
                condition.apply(criteria.getEstadoLaboralId(), copy.getEstadoLaboralId()) &&
                condition.apply(criteria.getCargoId(), copy.getCargoId()) &&
                condition.apply(criteria.getMedicoId(), copy.getMedicoId()) &&
                condition.apply(criteria.getEnfermeroId(), copy.getEnfermeroId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
