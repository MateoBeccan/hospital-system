package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicoCriteriaTest {

    @Test
    void newMedicoCriteriaHasAllFiltersNullTest() {
        var medicoCriteria = new MedicoCriteria();
        assertThat(medicoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicoCriteriaFluentMethodsCreatesFiltersTest() {
        var medicoCriteria = new MedicoCriteria();

        setAllFilters(medicoCriteria);

        assertThat(medicoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicoCriteriaCopyCreatesNullFilterTest() {
        var medicoCriteria = new MedicoCriteria();
        var copy = medicoCriteria.copy();

        assertThat(medicoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicoCriteria)
        );
    }

    @Test
    void medicoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicoCriteria = new MedicoCriteria();
        setAllFilters(medicoCriteria);

        var copy = medicoCriteria.copy();

        assertThat(medicoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicoCriteria = new MedicoCriteria();

        assertThat(medicoCriteria).hasToString("MedicoCriteria{}");
    }

    private static void setAllFilters(MedicoCriteria medicoCriteria) {
        medicoCriteria.id();
        medicoCriteria.matricula();
        medicoCriteria.fechaMatriculacion();
        medicoCriteria.firmaDigital();
        medicoCriteria.atiendeConsultorio();
        medicoCriteria.activo();
        medicoCriteria.fechaAlta();
        medicoCriteria.fechaBaja();
        medicoCriteria.empleadoId();
        medicoCriteria.especialidadId();
        medicoCriteria.distinct();
    }

    private static Condition<MedicoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMatricula()) &&
                condition.apply(criteria.getFechaMatriculacion()) &&
                condition.apply(criteria.getFirmaDigital()) &&
                condition.apply(criteria.getAtiendeConsultorio()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getEmpleadoId()) &&
                condition.apply(criteria.getEspecialidadId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicoCriteria> copyFiltersAre(MedicoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMatricula(), copy.getMatricula()) &&
                condition.apply(criteria.getFechaMatriculacion(), copy.getFechaMatriculacion()) &&
                condition.apply(criteria.getFirmaDigital(), copy.getFirmaDigital()) &&
                condition.apply(criteria.getAtiendeConsultorio(), copy.getAtiendeConsultorio()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getEmpleadoId(), copy.getEmpleadoId()) &&
                condition.apply(criteria.getEspecialidadId(), copy.getEspecialidadId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
