<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="empleado">
        <h2 class="jh-entity-heading" data-cy="empleadoDetailsHeading"><span>EmpleadoEmpleado</span> {{ empleado.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Legajo</span>
          </dt>
          <dd>
            <span>{{ empleado.legajo }}</span>
          </dd>
          <dt>
            <span>Fecha Ingreso</span>
          </dt>
          <dd>
            <span>{{ empleado.fechaIngreso }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ empleado.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ empleado.activo }}</span>
          </dd>
          <dt>
            <span>Persona</span>
          </dt>
          <dd>
            <div v-if="empleado.persona">
              <router-link :to="{ name: 'PersonaView', params: { personaId: empleado.persona.id } }">{{ empleado.persona.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Tipo Empleado</span>
          </dt>
          <dd>
            <div v-if="empleado.tipoEmpleado">
              <router-link :to="{ name: 'TipoEmpleadoView', params: { tipoEmpleadoId: empleado.tipoEmpleado.id } }">{{
                empleado.tipoEmpleado.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Estado Laboral</span>
          </dt>
          <dd>
            <div v-if="empleado.estadoLaboral">
              <router-link :to="{ name: 'EstadoLaboralView', params: { estadoLaboralId: empleado.estadoLaboral.id } }">{{
                empleado.estadoLaboral.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Cargo</span>
          </dt>
          <dd>
            <div v-if="empleado.cargo">
              <router-link :to="{ name: 'CargoView', params: { cargoId: empleado.cargo.id } }">{{ empleado.cargo.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="empleado.id" :to="{ name: 'EmpleadoEdit', params: { empleadoId: empleado.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./empleado-details.component.ts"></script>
