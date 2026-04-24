<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="grupoSanguineo">
        <h2 class="jh-entity-heading" data-cy="grupoSanguineoDetailsHeading">
          <span>Grupo SanguineoGrupoSanguineo</span> {{ grupoSanguineo.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.descripcion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ grupoSanguineo.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="grupoSanguineo.id"
          :to="{ name: 'GrupoSanguineoEdit', params: { grupoSanguineoId: grupoSanguineo.id } }"
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

<script lang="ts" src="./grupo-sanguineo-details.component.ts"></script>
