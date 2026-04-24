<template>
  <div>
    <h2 id="page-heading" data-cy="HistoriaClinicaHeading">
      <span id="historia-clinica">Historia Clinicas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'HistoriaClinicaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-historia-clinica"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Historia Clinica</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && historiaClinicas?.length === 0">
      <span>Ningún Historia Clinicas encontrado</span>
    </div>
    <div class="table-responsive" v-if="historiaClinicas?.length > 0">
      <table class="table table-striped" aria-describedby="historiaClinicas">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('numero')">
              <span>Numero</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numero'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaApertura')">
              <span>Fecha Apertura</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaApertura'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaUltimaActualizacion')">
              <span>Fecha Ultima Actualizacion</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'fechaUltimaActualizacion'"
              ></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('antecedentesPersonales')">
              <span>Antecedentes Personales</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'antecedentesPersonales'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('antecedentesFamiliares')">
              <span>Antecedentes Familiares</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'antecedentesFamiliares'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('enfermedadesPrevias')">
              <span>Enfermedades Previas</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'enfermedadesPrevias'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cirugiasPrevias')">
              <span>Cirugias Previas</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cirugiasPrevias'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('alergias')">
              <span>Alergias</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alergias'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('medicacionHabitual')">
              <span>Medicacion Habitual</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'medicacionHabitual'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('habitos')">
              <span>Habitos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'habitos'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observacionesGenerales')">
              <span>Observaciones Generales</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observacionesGenerales'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activa')">
              <span>Activa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activa'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaCierre')">
              <span>Fecha Cierre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaCierre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('motivoCierre')">
              <span>Motivo Cierre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'motivoCierre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('paciente.id')">
              <span>Paciente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="historiaClinica in historiaClinicas" :key="historiaClinica.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: historiaClinica.id } }">{{
                historiaClinica.id
              }}</router-link>
            </td>
            <td>{{ historiaClinica.numero }}</td>
            <td>{{ historiaClinica.fechaApertura }}</td>
            <td>{{ historiaClinica.fechaUltimaActualizacion }}</td>
            <td>{{ historiaClinica.antecedentesPersonales }}</td>
            <td>{{ historiaClinica.antecedentesFamiliares }}</td>
            <td>{{ historiaClinica.enfermedadesPrevias }}</td>
            <td>{{ historiaClinica.cirugiasPrevias }}</td>
            <td>{{ historiaClinica.alergias }}</td>
            <td>{{ historiaClinica.medicacionHabitual }}</td>
            <td>{{ historiaClinica.habitos }}</td>
            <td>{{ historiaClinica.observacionesGenerales }}</td>
            <td>{{ historiaClinica.activa }}</td>
            <td>{{ historiaClinica.fechaCierre }}</td>
            <td>{{ historiaClinica.motivoCierre }}</td>
            <td>
              <div v-if="historiaClinica.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: historiaClinica.paciente.id } }">{{
                  historiaClinica.paciente.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: historiaClinica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'HistoriaClinicaEdit', params: { historiaClinicaId: historiaClinica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(historiaClinica)"
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
        <span id="hospitalApp.historiaClinica.delete.question" data-cy="historiaClinicaDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-historiaClinica-heading">¿Seguro que quiere eliminar Historia Clinica {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-historiaClinica"
            data-cy="entityConfirmDeleteButton"
            @click="removeHistoriaClinica"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="historiaClinicas?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historia-clinica.component.ts"></script>
