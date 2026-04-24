<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="turnoLaboral">
        <h2 class="jh-entity-heading" data-cy="turnoLaboralDetailsHeading"><span>Turno LaboralTurnoLaboral</span> {{ turnoLaboral.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.nombre }}</span>
          </dd>
          <dt>
            <span>Hora Inicio</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.horaInicio }}</span>
          </dd>
          <dt>
            <span>Hora Fin</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.horaFin }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.descripcion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ turnoLaboral.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="turnoLaboral.id"
          :to="{ name: 'TurnoLaboralEdit', params: { turnoLaboralId: turnoLaboral.id } }"
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

<script lang="ts" src="./turno-laboral-details.component.ts"></script>
