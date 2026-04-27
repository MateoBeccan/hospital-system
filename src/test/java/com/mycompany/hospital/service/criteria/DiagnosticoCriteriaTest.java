package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DiagnosticoCriteriaTest {

    @Test
    void newDiagnosticoCriteriaHasAllFiltersNullTest() {
        var diagnosticoCriteria = new DiagnosticoCriteria();
        assertThat(diagnosticoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void diagnosticoCriteriaFluentMethodsCreatesFiltersTest() {
        var diagnosticoCriteria = new DiagnosticoCriteria();

        setAllFilters(diagnosticoCriteria);

        assertThat(diagnosticoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void diagnosticoCriteriaCopyCreatesNullFilterTest() {
        var diagnosticoCriteria = new DiagnosticoCriteria();
        var copy = diagnosticoCriteria.copy();

        assertThat(diagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(diagnosticoCriteria)
        );
    }

    @Test
    void diagnosticoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var diagnosticoCriteria = new DiagnosticoCriteria();
        setAllFilters(diagnosticoCriteria);

        var copy = diagnosticoCriteria.copy();

        assertThat(diagnosticoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(diagnosticoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var diagnosticoCriteria = new DiagnosticoCriteria();

        assertThat(diagnosticoCriteria).hasToString("DiagnosticoCriteria{}");
    }

    private static void setAllFilters(DiagnosticoCriteria diagnosticoCriteria) {
        diagnosticoCriteria.id();
        diagnosticoCriteria.codigo();
        diagnosticoCriteria.fechaDiagnostico();
        diagnosticoCriteria.descripcion();
        diagnosticoCriteria.activo();
        diagnosticoCriteria.fechaResolucion();
        diagnosticoCriteria.esPrincipal();
        diagnosticoCriteria.fechaAlta();
        diagnosticoCriteria.fechaBaja();
        diagnosticoCriteria.consultaId();
        diagnosticoCriteria.pacienteId();
        diagnosticoCriteria.medicoId();
        diagnosticoCriteria.tipoDiagnosticoId();
        diagnosticoCriteria.estadoDiagnosticoId();
        diagnosticoCriteria.distinct();
    }

    private static Condition<DiagnosticoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getFechaDiagnostico()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaResolucion()) &&
                condition.apply(criteria.getEsPrincipal()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getConsultaId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getMedicoId()) &&
                condition.apply(criteria.getTipoDiagnosticoId()) &&
                condition.apply(criteria.getEstadoDiagnosticoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DiagnosticoCriteria> copyFiltersAre(DiagnosticoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getFechaDiagnostico(), copy.getFechaDiagnostico()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaResolucion(), copy.getFechaResolucion()) &&
                condition.apply(criteria.getEsPrincipal(), copy.getEsPrincipal()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getConsultaId(), copy.getConsultaId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getMedicoId(), copy.getMedicoId()) &&
                condition.apply(criteria.getTipoDiagnosticoId(), copy.getTipoDiagnosticoId()) &&
                condition.apply(criteria.getEstadoDiagnosticoId(), copy.getEstadoDiagnosticoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
