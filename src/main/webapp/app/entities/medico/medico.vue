<template>
  <div>
    <h2 id="page-heading" data-cy="MedicoHeading">
      <span id="medico">Medicos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'MedicoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-medico"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Medico</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && medicos?.length === 0">
      <span>Ningún Medicos encontrado</span>
    </div>
    <div class="table-responsive" v-if="medicos?.length > 0">
      <table class="table table-striped" aria-describedby="medicos">
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
            <th scope="col" @click="changeOrder('firmaDigital')">
              <span>Firma Digital</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'firmaDigital'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('atiendeConsultorio')">
              <span>Atiende Consultorio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'atiendeConsultorio'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('especialidad.id')">
              <span>Especialidad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'especialidad.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="medico in medicos" :key="medico.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }">{{ medico.id }}</router-link>
            </td>
            <td>{{ medico.matricula }}</td>
            <td>{{ medico.fechaMatriculacion }}</td>
            <td>{{ medico.firmaDigital }}</td>
            <td>{{ medico.atiendeConsultorio }}</td>
            <td>{{ medico.activo }}</td>
            <td>{{ medico.fechaAlta }}</td>
            <td>{{ medico.fechaBaja }}</td>
            <td>
              <div v-if="medico.empleado">
                <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: medico.empleado.id } }">{{
                  medico.empleado.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="medico.especialidad">
                <router-link :to="{ name: 'EspecialidadView', params: { especialidadId: medico.especialidad.id } }">{{
                  medico.especialidad.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MedicoEdit', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(medico)"
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
        <span id="hospitalApp.medico.delete.question" data-cy="medicoDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-medico-heading">¿Seguro que quiere eliminar Medico {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-medico"
            data-cy="entityConfirmDeleteButton"
            @click="removeMedico"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="medicos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medico.component.ts"></script>
