package com.mycompany.hospital.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PersonaCriteriaTest {

    @Test
    void newPersonaCriteriaHasAllFiltersNullTest() {
        var personaCriteria = new PersonaCriteria();
        assertThat(personaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void personaCriteriaFluentMethodsCreatesFiltersTest() {
        var personaCriteria = new PersonaCriteria();

        setAllFilters(personaCriteria);

        assertThat(personaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void personaCriteriaCopyCreatesNullFilterTest() {
        var personaCriteria = new PersonaCriteria();
        var copy = personaCriteria.copy();

        assertThat(personaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(personaCriteria)
        );
    }

    @Test
    void personaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var personaCriteria = new PersonaCriteria();
        setAllFilters(personaCriteria);

        var copy = personaCriteria.copy();

        assertThat(personaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(personaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var personaCriteria = new PersonaCriteria();

        assertThat(personaCriteria).hasToString("PersonaCriteria{}");
    }

    private static void setAllFilters(PersonaCriteria personaCriteria) {
        personaCriteria.id();
        personaCriteria.nombre();
        personaCriteria.apellido();
        personaCriteria.nroDocumento();
        personaCriteria.fechaNacimiento();
        personaCriteria.telefono();
        personaCriteria.email();
        personaCriteria.direccion();
        personaCriteria.activo();
        personaCriteria.fechaAlta();
        personaCriteria.fechaBaja();
        personaCriteria.tipoDocumentoId();
        personaCriteria.sexoId();
        personaCriteria.ciudadId();
        personaCriteria.pacienteId();
        personaCriteria.empleadoId();
        personaCriteria.distinct();
    }

    private static Condition<PersonaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getApellido()) &&
                condition.apply(criteria.getNroDocumento()) &&
                condition.apply(criteria.getFechaNacimiento()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDireccion()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja()) &&
                condition.apply(criteria.getTipoDocumentoId()) &&
                condition.apply(criteria.getSexoId()) &&
                condition.apply(criteria.getCiudadId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getEmpleadoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PersonaCriteria> copyFiltersAre(PersonaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getApellido(), copy.getApellido()) &&
                condition.apply(criteria.getNroDocumento(), copy.getNroDocumento()) &&
                condition.apply(criteria.getFechaNacimiento(), copy.getFechaNacimiento()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDireccion(), copy.getDireccion()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getFechaAlta(), copy.getFechaAlta()) &&
                condition.apply(criteria.getFechaBaja(), copy.getFechaBaja()) &&
                condition.apply(criteria.getTipoDocumentoId(), copy.getTipoDocumentoId()) &&
                condition.apply(criteria.getSexoId(), copy.getSexoId()) &&
                condition.apply(criteria.getCiudadId(), copy.getCiudadId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getEmpleadoId(), copy.getEmpleadoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
