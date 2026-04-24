<template>
  <div>
    <h2 id="page-heading" data-cy="CiudadHeading">
      <span id="ciudad">Ciudads</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'CiudadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-ciudad"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Ciudad</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && ciudads?.length === 0">
      <span>Ningún Ciudads encontrado</span>
    </div>
    <div class="table-responsive" v-if="ciudads?.length > 0">
      <table class="table table-striped" aria-describedby="ciudads">
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
            <th scope="col" @click="changeOrder('codigoPostal')">
              <span>Codigo Postal</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigoPostal'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('provincia.id')">
              <span>Provincia</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'provincia.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ciudad in ciudads" :key="ciudad.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CiudadView', params: { ciudadId: ciudad.id } }">{{ ciudad.id }}</router-link>
            </td>
            <td>{{ ciudad.nombre }}</td>
            <td>{{ ciudad.codigo }}</td>
            <td>{{ ciudad.codigoPostal }}</td>
            <td>{{ ciudad.fechaAlta }}</td>
            <td>{{ ciudad.fechaBaja }}</td>
            <td>{{ ciudad.activo }}</td>
            <td>
              <div v-if="ciudad.provincia">
                <router-link :to="{ name: 'ProvinciaView', params: { provinciaId: ciudad.provincia.id } }">{{
                  ciudad.provincia.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'CiudadView', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CiudadEdit', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(ciudad)"
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
        <span id="hospitalApp.ciudad.delete.question" data-cy="ciudadDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-ciudad-heading">¿Seguro que quiere eliminar Ciudad {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-ciudad"
            data-cy="entityConfirmDeleteButton"
            @click="removeCiudad"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="ciudads?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ciudad.component.ts"></script>
