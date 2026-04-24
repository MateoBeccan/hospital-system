<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="tipoEmpleado">
        <h2 class="jh-entity-heading" data-cy="tipoEmpleadoDetailsHeading"><span>Tipo EmpleadoTipoEmpleado</span> {{ tipoEmpleado.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.descripcion }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ tipoEmpleado.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="tipoEmpleado.id"
          :to="{ name: 'TipoEmpleadoEdit', params: { tipoEmpleadoId: tipoEmpleado.id } }"
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

<script lang="ts" src="./tipo-empleado-details.component.ts"></script>
