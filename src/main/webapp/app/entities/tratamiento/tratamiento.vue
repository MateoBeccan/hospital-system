<template>
  <div>
    <h2 id="page-heading" data-cy="TratamientoHeading">
      <span id="tratamiento">Tratamientos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'TratamientoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-tratamiento"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Tratamiento</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tratamientos?.length === 0">
      <span>Ningún Tratamientos encontrado</span>
    </div>
    <div class="table-responsive" v-if="tratamientos?.length > 0">
      <table class="table table-striped" aria-describedby="tratamientos">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaInicio')">
              <span>Fecha Inicio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaInicio'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaFin')">
              <span>Fecha Fin</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaFin'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaProximaRevision')">
              <span>Fecha Proxima Revision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaProximaRevision'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('diagnostico.id')">
              <span>Diagnostico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'diagnostico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('estadoTratamiento.id')">
              <span>Estado Tratamiento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estadoTratamiento.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tratamiento in tratamientos" :key="tratamiento.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TratamientoView', params: { tratamientoId: tratamiento.id } }">{{ tratamiento.id }}</router-link>
            </td>
            <td>{{ tratamiento.codigo }}</td>
            <td>{{ tratamiento.descripcion }}</td>
            <td>{{ tratamiento.fechaInicio }}</td>
            <td>{{ tratamiento.fechaFin }}</td>
            <td>{{ tratamiento.observaciones }}</td>
            <td>{{ tratamiento.fechaProximaRevision }}</td>
            <td>{{ tratamiento.activo }}</td>
            <td>{{ tratamiento.fechaAlta }}</td>
            <td>{{ tratamiento.fechaBaja }}</td>
            <td>
              <div v-if="tratamiento.diagnostico">
                <router-link :to="{ name: 'DiagnosticoView', params: { diagnosticoId: tratamiento.diagnostico.id } }">{{
                  tratamiento.diagnostico.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="tratamiento.estadoTratamiento">
                <router-link :to="{ name: 'EstadoTratamientoView', params: { estadoTratamientoId: tratamiento.estadoTratamiento.id } }">{{
                  tratamiento.estadoTratamiento.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'TratamientoView', params: { tratamientoId: tratamiento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TratamientoEdit', params: { tratamientoId: tratamiento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(tratamiento)"
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
        <span id="hospitalApp.tratamiento.delete.question" data-cy="tratamientoDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-tratamiento-heading">¿Seguro que quiere eliminar Tratamiento {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-tratamiento"
            data-cy="entityConfirmDeleteButton"
            @click="removeTratamiento"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="tratamientos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tratamiento.component.ts"></script>
