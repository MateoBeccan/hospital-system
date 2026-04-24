<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="cargo">
        <h2 class="jh-entity-heading" data-cy="cargoDetailsHeading"><span>CargoCargo</span> {{ cargo.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ cargo.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ cargo.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ cargo.descripcion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ cargo.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ cargo.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ cargo.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="cargo.id" :to="{ name: 'CargoEdit', params: { cargoId: cargo.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cargo-details.component.ts"></script>
