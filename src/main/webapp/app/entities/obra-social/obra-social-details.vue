<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="obraSocial">
        <h2 class="jh-entity-heading" data-cy="obraSocialDetailsHeading"><span>Obra SocialObraSocial</span> {{ obraSocial.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ obraSocial.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ obraSocial.nombre }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ obraSocial.telefono }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ obraSocial.email }}</span>
          </dd>
          <dt>
            <span>Direccion</span>
          </dt>
          <dd>
            <span>{{ obraSocial.direccion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ obraSocial.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ obraSocial.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ obraSocial.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="obraSocial.id"
          :to="{ name: 'ObraSocialEdit', params: { obraSocialId: obraSocial.id } }"
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

<script lang="ts" src="./obra-social-details.component.ts"></script>
