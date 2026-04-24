<template>
  <div>
    <h2 id="page-heading" data-cy="GrupoSanguineoHeading">
      <span id="grupo-sanguineo">Grupo Sanguineos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'GrupoSanguineoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-grupo-sanguineo"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Grupo Sanguineo</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && grupoSanguineos?.length === 0">
      <span>Ningún Grupo Sanguineos encontrado</span>
    </div>
    <div class="table-responsive" v-if="grupoSanguineos?.length > 0">
      <table class="table table-striped" aria-describedby="grupoSanguineos">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
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
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="grupoSanguineo in grupoSanguineos" :key="grupoSanguineo.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'GrupoSanguineoView', params: { grupoSanguineoId: grupoSanguineo.id } }">{{
                grupoSanguineo.id
              }}</router-link>
            </td>
            <td>{{ grupoSanguineo.codigo }}</td>
            <td>{{ grupoSanguineo.nombre }}</td>
            <td>{{ grupoSanguineo.descripcion }}</td>
            <td>{{ grupoSanguineo.activo }}</td>
            <td>{{ grupoSanguineo.fechaAlta }}</td>
            <td>{{ grupoSanguineo.fechaBaja }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'GrupoSanguineoView', params: { grupoSanguineoId: grupoSanguineo.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'GrupoSanguineoEdit', params: { grupoSanguineoId: grupoSanguineo.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(grupoSanguineo)"
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
        <span id="hospitalApp.grupoSanguineo.delete.question" data-cy="grupoSanguineoDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-grupoSanguineo-heading">¿Seguro que quiere eliminar Grupo Sanguineo {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-grupoSanguineo"
            data-cy="entityConfirmDeleteButton"
            @click="removeGrupoSanguineo"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="grupoSanguineos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./grupo-sanguineo.component.ts"></script>
