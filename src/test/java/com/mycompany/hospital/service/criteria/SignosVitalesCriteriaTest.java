package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SignosVitalesCriteriaTest {

    @Test
    void newSignosVitalesCriteriaHasAllFiltersNullTest() {
        var signosVitalesCriteria = new SignosVitalesCriteria();
        assertThat(signosVitalesCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void signosVitalesCriteriaFluentMethodsCreatesFiltersTest() {
        var signosVitalesCriteria = new SignosVitalesCriteria();

        setAllFilters(signosVitalesCriteria);

        assertThat(signosVitalesCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void signosVitalesCriteriaCopyCreatesNullFilterTest() {
        var signosVitalesCriteria = new SignosVitalesCriteria();
        var copy = signosVitalesCriteria.copy();

        assertThat(signosVitalesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(signosVitalesCriteria)
        );
    }

    @Test
    void signosVitalesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var signosVitalesCriteria = new SignosVitalesCriteria();
        setAllFilters(signosVitalesCriteria);

        var copy = signosVitalesCriteria.copy();

        assertThat(signosVitalesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(signosVitalesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var signosVitalesCriteria = new SignosVitalesCriteria();

        assertThat(signosVitalesCriteria).hasToString("SignosVitalesCriteria{}");
    }

    private static void setAllFilters(SignosVitalesCriteria signosVitalesCriteria) {
        signosVitalesCriteria.id();
        signosVitalesCriteria.fechaHoraRegistro();
        signosVitalesCriteria.peso();
        signosVitalesCriteria.talla();
        signosVitalesCriteria.temperatura();
        signosVitalesCriteria.presionArterial();
        signosVitalesCriteria.frecuenciaCardiaca();
        signosVitalesCriteria.frecuenciaRespiratoria();
        signosVitalesCriteria.saturacionOxigeno();
        signosVitalesCriteria.activo();
        signosVitalesCriteria.fechaAlta();
        signosVitalesCriteria.fechaBaja();
        signosVitalesCriteria.consultaId();
        signosVitalesCriteria.distinct();
    }

    private static Condition<SignosVitalesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaHoraRegistro()) &&
                condition.apply(criteria.getPeso()) &&
                condition.apply(criteria.getTalla()) &&
                condition.apply(criteria.getTemperatura()) &&
                condition.apply(criteria.getPresionArterial()) &&
                condition.apply(criteria.getFrecuenciaCardiaca()) &&
                condition.apply(criteria.getFrecuenciaRespiratoria()) &&
                condition.apply(criteria.getSaturacionOxigeno()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getConsultaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SignosVitalesCriteria> copyFiltersAre(
        SignosVitalesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaHoraRegistro(), copy.getFechaHoraRegistro()) &&
                condition.apply(criteria.getPeso(), copy.getPeso()) &&
                condition.apply(criteria.getTalla(), copy.getTalla()) &&
                condition.apply(criteria.getTemperatura(), copy.getTemperatura()) &&
                condition.apply(criteria.getPresionArterial(), copy.getPresionArterial()) &&
                condition.apply(criteria.getFrecuenciaCardiaca(), copy.getFrecuenciaCardiaca()) &&
                condition.apply(criteria.getFrecuenciaRespiratoria(), copy.getFrecuenciaRespiratoria()) &&
                condition.apply(criteria.getSaturacionOxigeno(), copy.getSaturacionOxigeno()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getConsultaId(), copy.getConsultaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
