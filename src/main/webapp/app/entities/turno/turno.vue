<template>
  <div>
    <h2 id="page-heading" data-cy="TurnoHeading">
      <span id="turno">Turnos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'TurnoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-turno"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Turno</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && turnos?.length === 0">
      <span>Ningún Turnos encontrado</span>
    </div>
    <div class="table-responsive" v-if="turnos?.length > 0">
      <table class="table table-striped" aria-describedby="turnos">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaHora')">
              <span>Fecha Hora</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaHora'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('duracionMinutos')">
              <span>Duracion Minutos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'duracionMinutos'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('motivoConsulta')">
              <span>Motivo Consulta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'motivoConsulta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaCreacion')">
              <span>Fecha Creacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaCreacion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaAlta')">
              <span>Fecha Alta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaAlta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaBaja')">
              <span>Fecha Baja</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaBaja'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('paciente.id')">
              <span>Paciente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('medico.id')">
              <span>Medico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'medico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('especialidad.id')">
              <span>Especialidad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'especialidad.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('estadoTurno.id')">
              <span>Estado Turno</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estadoTurno.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('canalSolicitud.id')">
              <span>Canal Solicitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'canalSolicitud.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="turno in turnos" :key="turno.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TurnoView', params: { turnoId: turno.id } }">{{ turno.id }}</router-link>
            </td>
            <td>{{ turno.codigo }}</td>
            <td>{{ formatDateShort(turno.fechaHora) || '' }}</td>
            <td>{{ turno.duracionMinutos }}</td>
            <td>{{ turno.motivoConsulta }}</td>
            <td>{{ turno.observaciones }}</td>
            <td>{{ formatDateShort(turno.fechaCreacion) || '' }}</td>
            <td>{{ turno.activo }}</td>
            <td>{{ turno.fechaAlta }}</td>
            <td>{{ turno.fechaBaja }}</td>
            <td>
              <div v-if="turno.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: turno.paciente.id } }">{{ turno.paciente.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="turno.medico">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: turno.medico.id } }">{{ turno.medico.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="turno.especialidad">
                <router-link :to="{ name: 'EspecialidadView', params: { especialidadId: turno.especialidad.id } }">{{
                  turno.especialidad.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="turno.estadoTurno">
                <router-link :to="{ name: 'EstadoTurnoView', params: { estadoTurnoId: turno.estadoTurno.id } }">{{
                  turno.estadoTurno.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="turno.canalSolicitud">
                <router-link :to="{ name: 'CanalSolicitudView', params: { canalSolicitudId: turno.canalSolicitud.id } }">{{
                  turno.canalSolicitud.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'TurnoView', params: { turnoId: turno.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TurnoEdit', params: { turnoId: turno.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(turno)"
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
        <span id="hospitalApp.turno.delete.question" data-cy="turnoDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-turno-heading">¿Seguro que quiere eliminar Turno {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-turno"
            data-cy="entityConfirmDeleteButton"
            @click="removeTurno"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="turnos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./turno.component.ts"></script>
