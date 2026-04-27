<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="tipoDiagnostico">
        <h2 class="jh-entity-heading" data-cy="tipoDiagnosticoDetailsHeading">
          <span>Tipo DiagnosticoTipoDiagnostico</span> {{ tipoDiagnostico.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.codigo }}</span>
          </dd>
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.descripcion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ tipoDiagnostico.fechaBaja }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="tipoDiagnostico.id"
          :to="{ name: 'TipoDiagnosticoEdit', params: { tipoDiagnosticoId: tipoDiagnostico.id } }"
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

<script lang="ts" src="./tipo-diagnostico-details.component.ts"></script>
