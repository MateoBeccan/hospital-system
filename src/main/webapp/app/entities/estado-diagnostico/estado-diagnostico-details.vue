<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="estadoDiagnostico">
        <h2 class="jh-entity-heading" data-cy="estadoDiagnosticoDetailsHeading">
          <span>Estado DiagnosticoEstadoDiagnostico</span> {{ estadoDiagnostico.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.descripcion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ estadoDiagnostico.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="estadoDiagnostico.id"
          :to="{ name: 'EstadoDiagnosticoEdit', params: { estadoDiagnosticoId: estadoDiagnostico.id } }"
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

<script lang="ts" src="./estado-diagnostico-details.component.ts"></script>
