<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="enfermero">
        <h2 class="jh-entity-heading" data-cy="enfermeroDetailsHeading"><span>EnfermeroEnfermero</span> {{ enfermero.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Matricula</span>
          </dt>
          <dd>
            <span>{{ enfermero.matricula }}</span>
          </dd>
          <dt>
            <span>Fecha Matriculacion</span>
          </dt>
          <dd>
            <span>{{ enfermero.fechaMatriculacion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ enfermero.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ enfermero.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ enfermero.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Empleado</span>
          </dt>
          <dd>
            <div v-if="enfermero.empleado">
              <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: enfermero.empleado.id } }">{{
                enfermero.empleado.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Turno Laboral</span>
          </dt>
          <dd>
            <div v-if="enfermero.turnoLaboral">
              <router-link :to="{ name: 'TurnoLaboralView', params: { turnoLaboralId: enfermero.turnoLaboral.id } }">{{
                enfermero.turnoLaboral.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="enfermero.id"
          :to="{ name: 'EnfermeroEdit', params: { enfermeroId: enfermero.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./enfermero-details.component.ts"></script>
