<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="provincia">
        <h2 class="jh-entity-heading" data-cy="provinciaDetailsHeading"><span>ProvinciaProvincia</span> {{ provincia.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ provincia.nombre }}</span>
          </dd>
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ provincia.codigo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ provincia.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ provincia.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ provincia.activo }}</span>
          </dd>
          <dt>
            <span>Pais</span>
          </dt>
          <dd>
            <div v-if="provincia.pais">
              <router-link :to="{ name: 'PaisView', params: { paisId: provincia.pais.id } }">{{ provincia.pais.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="provincia.id"
          :to="{ name: 'ProvinciaEdit', params: { provinciaId: provincia.id } }"
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

<script lang="ts" src="./provincia-details.component.ts"></script>
