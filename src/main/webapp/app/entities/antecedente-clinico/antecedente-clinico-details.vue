<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="antecedenteClinico">
        <h2 class="jh-entity-heading" data-cy="antecedenteClinicoDetailsHeading">
          <span>Antecedente ClinicoAntecedenteClinico</span> {{ antecedenteClinico.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Titulo</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.titulo }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.descripcion }}</span>
          </dd>
          <dt>
            <span>Fecha Registro</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.fechaRegistro }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.observaciones }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ antecedenteClinico.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Historia Clinica</span>
          </dt>
          <dd>
            <div v-if="antecedenteClinico.historiaClinica">
              <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: antecedenteClinico.historiaClinica.id } }">{{
                antecedenteClinico.historiaClinica.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="antecedenteClinico.id"
          :to="{ name: 'AntecedenteClinicoEdit', params: { antecedenteClinicoId: antecedenteClinico.id } }"
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

<script lang="ts" src="./antecedente-clinico-details.component.ts"></script>
