<template>
  <div>
    <h2 id="page-heading" data-cy="ProvinciaHeading">
      <span id="provincia">Provincias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'ProvinciaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-provincia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Provincia</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && provincias?.length === 0">
      <span>Ningún Provincias encontrado</span>
    </div>
    <div class="table-responsive" v-if="provincias?.length > 0">
      <table class="table table-striped" aria-describedby="provincias">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('pais.id')">
              <span>Pais</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'pais.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="provincia in provincias" :key="provincia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProvinciaView', params: { provinciaId: provincia.id } }">{{ provincia.id }}</router-link>
            </td>
            <td>{{ provincia.nombre }}</td>
            <td>{{ provincia.codigo }}</td>
            <td>{{ provincia.fechaAlta }}</td>
            <td>{{ provincia.fechaBaja }}</td>
            <td>{{ provincia.activo }}</td>
            <td>
              <div v-if="provincia.pais">
                <router-link :to="{ name: 'PaisView', params: { paisId: provincia.pais.id } }">{{ provincia.pais.id }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'ProvinciaView', params: { provinciaId: provincia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProvinciaEdit', params: { provinciaId: provincia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(provincia)"
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
        <span id="hospitalApp.provincia.delete.question" data-cy="provinciaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-provincia-heading">¿Seguro que quiere eliminar Provincia {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-provincia"
            data-cy="entityConfirmDeleteButton"
            @click="removeProvincia"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="provincias?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./provincia.component.ts"></script>
