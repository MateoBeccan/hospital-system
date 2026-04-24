<template>
  <div>
    <h2 id="page-heading" data-cy="EnfermeroHeading">
      <span id="enfermero">Enfermeros</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'EnfermeroCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-enfermero"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Enfermero</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && enfermeros?.length === 0">
      <span>Ningún Enfermeros encontrado</span>
    </div>
    <div class="table-responsive" v-if="enfermeros?.length > 0">
      <table class="table table-striped" aria-describedby="enfermeros">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('matricula')">
              <span>Matricula</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'matricula'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaMatriculacion')">
              <span>Fecha Matriculacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaMatriculacion'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('empleado.id')">
              <span>Empleado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'empleado.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('turnoLaboral.id')">
              <span>Turno Laboral</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'turnoLaboral.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="enfermero in enfermeros" :key="enfermero.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EnfermeroView', params: { enfermeroId: enfermero.id } }">{{ enfermero.id }}</router-link>
            </td>
            <td>{{ enfermero.matricula }}</td>
            <td>{{ enfermero.fechaMatriculacion }}</td>
            <td>{{ enfermero.activo }}</td>
            <td>{{ enfermero.fechaAlta }}</td>
            <td>{{ enfermero.fechaBaja }}</td>
            <td>
              <div v-if="enfermero.empleado">
                <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: enfermero.empleado.id } }">{{
                  enfermero.empleado.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="enfermero.turnoLaboral">
                <router-link :to="{ name: 'TurnoLaboralView', params: { turnoLaboralId: enfermero.turnoLaboral.id } }">{{
                  enfermero.turnoLaboral.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'EnfermeroView', params: { enfermeroId: enfermero.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EnfermeroEdit', params: { enfermeroId: enfermero.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(enfermero)"
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
        <span id="hospitalApp.enfermero.delete.question" data-cy="enfermeroDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-enfermero-heading">¿Seguro que quiere eliminar Enfermero {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-enfermero"
            data-cy="entityConfirmDeleteButton"
            @click="removeEnfermero"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="enfermeros?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./enfermero.component.ts"></script>
