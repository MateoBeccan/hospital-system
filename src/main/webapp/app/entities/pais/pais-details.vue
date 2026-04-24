<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="pais">
        <h2 class="jh-entity-heading" data-cy="paisDetailsHeading"><span>PaisPais</span> {{ pais.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ pais.nombre }}</span>
          </dd>
          <dt>
            <span>Codigo Iso</span>
          </dt>
          <dd>
            <span>{{ pais.codigoIso }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ pais.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ pais.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ pais.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="pais.id" :to="{ name: 'PaisEdit', params: { paisId: pais.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pais-details.component.ts"></script>
