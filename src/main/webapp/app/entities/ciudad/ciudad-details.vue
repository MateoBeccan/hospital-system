<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="ciudad">
        <h2 class="jh-entity-heading" data-cy="ciudadDetailsHeading"><span>CiudadCiudad</span> {{ ciudad.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ ciudad.nombre }}</span>
          </dd>
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ ciudad.codigo }}</span>
          </dd>
          <dt>
            <span>Codigo Postal</span>
          </dt>
          <dd>
            <span>{{ ciudad.codigoPostal }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ ciudad.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ ciudad.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ ciudad.activo }}</span>
          </dd>
          <dt>
            <span>Provincia</span>
          </dt>
          <dd>
            <div v-if="ciudad.provincia">
              <router-link :to="{ name: 'ProvinciaView', params: { provinciaId: ciudad.provincia.id } }">{{
                ciudad.provincia.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="ciudad.id" :to="{ name: 'CiudadEdit', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ciudad-details.component.ts"></script>
