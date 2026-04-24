<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="paciente">
        <h2 class="jh-entity-heading" data-cy="pacienteDetailsHeading"><span>PacientePaciente</span> {{ paciente.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Numero Historia Clinica</span>
          </dt>
          <dd>
            <span>{{ paciente.numeroHistoriaClinica }}</span>
          </dd>
          <dt>
            <span>Alergias Generales</span>
          </dt>
          <dd>
            <span>{{ paciente.alergiasGenerales }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ paciente.observaciones }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ paciente.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ paciente.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ paciente.activo }}</span>
          </dd>
          <dt>
            <span>Persona</span>
          </dt>
          <dd>
            <div v-if="paciente.persona">
              <router-link :to="{ name: 'PersonaView', params: { personaId: paciente.persona.id } }">{{ paciente.persona.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Obra Social</span>
          </dt>
          <dd>
            <div v-if="paciente.obraSocial">
              <router-link :to="{ name: 'ObraSocialView', params: { obraSocialId: paciente.obraSocial.id } }">{{
                paciente.obraSocial.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Grupo Sanguineo</span>
          </dt>
          <dd>
            <div v-if="paciente.grupoSanguineo">
              <router-link :to="{ name: 'GrupoSanguineoView', params: { grupoSanguineoId: paciente.grupoSanguineo.id } }">{{
                paciente.grupoSanguineo.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Factor Rh</span>
          </dt>
          <dd>
            <div v-if="paciente.factorRh">
              <router-link :to="{ name: 'FactorRhView', params: { factorRhId: paciente.factorRh.id } }">{{
                paciente.factorRh.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="paciente.id" :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente-details.component.ts"></script>
