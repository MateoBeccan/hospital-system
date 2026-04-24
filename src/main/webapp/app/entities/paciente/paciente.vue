<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteHeading">
      <span id="paciente">Pacientes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'PacienteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paciente"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Paciente</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacientes?.length === 0">
      <span>Ningún Pacientes encontrado</span>
    </div>
    <div class="table-responsive" v-if="pacientes?.length > 0">
      <table class="table table-striped" aria-describedby="pacientes">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('numeroHistoriaClinica')">
              <span>Numero Historia Clinica</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroHistoriaClinica'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('alergiasGenerales')">
              <span>Alergias Generales</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alergiasGenerales'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaAlta')">
              <span>Fecha Alta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaAlta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaBaja')">
              <span>Fecha Baja</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaBaja'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('persona.id')">
              <span>Persona</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'persona.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('obraSocial.id')">
              <span>Obra Social</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'obraSocial.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('grupoSanguineo.id')">
              <span>Grupo Sanguineo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'grupoSanguineo.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('factorRh.id')">
              <span>Factor Rh</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'factorRh.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paciente in pacientes" :key="paciente.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }">{{ paciente.id }}</router-link>
            </td>
            <td>{{ paciente.numeroHistoriaClinica }}</td>
            <td>{{ paciente.alergiasGenerales }}</td>
            <td>{{ paciente.observaciones }}</td>
            <td>{{ paciente.fechaAlta }}</td>
            <td>{{ paciente.fechaBaja }}</td>
            <td>{{ paciente.activo }}</td>
            <td>
              <div v-if="paciente.persona">
                <router-link :to="{ name: 'PersonaView', params: { personaId: paciente.persona.id } }">{{
                  paciente.persona.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="paciente.obraSocial">
                <router-link :to="{ name: 'ObraSocialView', params: { obraSocialId: paciente.obraSocial.id } }">{{
                  paciente.obraSocial.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="paciente.grupoSanguineo">
                <router-link :to="{ name: 'GrupoSanguineoView', params: { grupoSanguineoId: paciente.grupoSanguineo.id } }">{{
                  paciente.grupoSanguineo.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="paciente.factorRh">
                <router-link :to="{ name: 'FactorRhView', params: { factorRhId: paciente.factorRh.id } }">{{
                  paciente.factorRh.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(paciente)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Eliminar</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="hospitalApp.paciente.delete.question" data-cy="pacienteDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paciente-heading">¿Seguro que quiere eliminar Paciente {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paciente"
            data-cy="entityConfirmDeleteButton"
            @click="removePaciente"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="pacientes?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente.component.ts"></script>
