<template>
  <div>
    <h2 id="page-heading" data-cy="ConsultaHeading">
      <span id="consulta">Consultas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'ConsultaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-consulta"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Consulta</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && consultas?.length === 0">
      <span>Ningún Consultas encontrado</span>
    </div>
    <div class="table-responsive" v-if="consultas?.length > 0">
      <table class="table table-striped" aria-describedby="consultas">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaHoraInicio')">
              <span>Fecha Hora Inicio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaHoraInicio'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaHoraFin')">
              <span>Fecha Hora Fin</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaHoraFin'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sintomas')">
              <span>Sintomas</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sintomas'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('motivoConsulta')">
              <span>Motivo Consulta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'motivoConsulta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('examenFisico')">
              <span>Examen Fisico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'examenFisico'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('indicaciones')">
              <span>Indicaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'indicaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activa')">
              <span>Activa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activa'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaAlta')">
              <span>Fecha Alta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaAlta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaBaja')">
              <span>Fecha Baja</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaBaja'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('turno.id')">
              <span>Turno</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'turno.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('paciente.id')">
              <span>Paciente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('medico.id')">
              <span>Medico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'medico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('historiaClinica.id')">
              <span>Historia Clinica</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'historiaClinica.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="consulta in consultas" :key="consulta.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ConsultaView', params: { consultaId: consulta.id } }">{{ consulta.id }}</router-link>
            </td>
            <td>{{ consulta.codigo }}</td>
            <td>{{ formatDateShort(consulta.fechaHoraInicio) || '' }}</td>
            <td>{{ formatDateShort(consulta.fechaHoraFin) || '' }}</td>
            <td>{{ consulta.sintomas }}</td>
            <td>{{ consulta.motivoConsulta }}</td>
            <td>{{ consulta.examenFisico }}</td>
            <td>{{ consulta.observaciones }}</td>
            <td>{{ consulta.indicaciones }}</td>
            <td>{{ consulta.activa }}</td>
            <td>{{ consulta.fechaAlta }}</td>
            <td>{{ consulta.fechaBaja }}</td>
            <td>
              <div v-if="consulta.turno">
                <router-link :to="{ name: 'TurnoView', params: { turnoId: consulta.turno.id } }">{{ consulta.turno.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="consulta.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: consulta.paciente.id } }">{{
                  consulta.paciente.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="consulta.medico">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: consulta.medico.id } }">{{ consulta.medico.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="consulta.historiaClinica">
                <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: consulta.historiaClinica.id } }">{{
                  consulta.historiaClinica.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'ConsultaView', params: { consultaId: consulta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ConsultaEdit', params: { consultaId: consulta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(consulta)"
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
        <span id="hospitalApp.consulta.delete.question" data-cy="consultaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-consulta-heading">¿Seguro que quiere eliminar Consulta {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-consulta"
            data-cy="entityConfirmDeleteButton"
            @click="removeConsulta"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="consultas?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./consulta.component.ts"></script>
