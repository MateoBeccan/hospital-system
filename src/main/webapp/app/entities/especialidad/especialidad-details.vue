<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="especialidad">
        <h2 class="jh-entity-heading" data-cy="especialidadDetailsHeading"><span>EspecialidadEspecialidad</span> {{ especialidad.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ especialidad.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ especialidad.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ especialidad.descripcion }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ especialidad.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ especialidad.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activa</span>
          </dt>
          <dd>
            <span>{{ especialidad.activa }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="especialidad.id"
          :to="{ name: 'EspecialidadEdit', params: { especialidadId: especialidad.id } }"
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

<script lang="ts" src="./especialidad-details.component.ts"></script>
