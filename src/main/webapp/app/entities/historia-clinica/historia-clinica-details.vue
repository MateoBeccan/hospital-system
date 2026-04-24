<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="historiaClinica">
        <h2 class="jh-entity-heading" data-cy="historiaClinicaDetailsHeading">
          <span>Historia ClinicaHistoriaClinica</span> {{ historiaClinica.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Numero</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.numero }}</span>
          </dd>
          <dt>
            <span>Fecha Apertura</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.fechaApertura }}</span>
          </dd>
          <dt>
            <span>Fecha Ultima Actualizacion</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.fechaUltimaActualizacion }}</span>
          </dd>
          <dt>
            <span>Antecedentes Personales</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.antecedentesPersonales }}</span>
          </dd>
          <dt>
            <span>Antecedentes Familiares</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.antecedentesFamiliares }}</span>
          </dd>
          <dt>
            <span>Enfermedades Previas</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.enfermedadesPrevias }}</span>
          </dd>
          <dt>
            <span>Cirugias Previas</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.cirugiasPrevias }}</span>
          </dd>
          <dt>
            <span>Alergias</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.alergias }}</span>
          </dd>
          <dt>
            <span>Medicacion Habitual</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.medicacionHabitual }}</span>
          </dd>
          <dt>
            <span>Habitos</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.habitos }}</span>
          </dd>
          <dt>
            <span>Observaciones Generales</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.observacionesGenerales }}</span>
          </dd>
          <dt>
            <span>Activa</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.activa }}</span>
          </dd>
          <dt>
            <span>Fecha Cierre</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.fechaCierre }}</span>
          </dd>
          <dt>
            <span>Motivo Cierre</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.motivoCierre }}</span>
          </dd>
          <dt>
            <span>Paciente</span>
          </dt>
          <dd>
            <div v-if="historiaClinica.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: historiaClinica.paciente.id } }">{{
                historiaClinica.paciente.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="historiaClinica.id"
          :to="{ name: 'HistoriaClinicaEdit', params: { historiaClinicaId: historiaClinica.id } }"
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

<script lang="ts" src="./historia-clinica-details.component.ts"></script>
