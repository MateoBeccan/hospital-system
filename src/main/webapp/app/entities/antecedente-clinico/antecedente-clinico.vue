<template>
  <div>
    <h2 id="page-heading" data-cy="AntecedenteClinicoHeading">
      <span id="antecedente-clinico">Antecedente Clinicos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'AntecedenteClinicoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-antecedente-clinico"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Antecedente Clinico</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && antecedenteClinicos?.length === 0">
      <span>Ningún Antecedente Clinicos encontrado</span>
    </div>
    <div class="table-responsive" v-if="antecedenteClinicos?.length > 0">
      <table class="table table-striped" aria-describedby="antecedenteClinicos">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('titulo')">
              <span>Titulo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titulo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaRegistro')">
              <span>Fecha Registro</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaRegistro'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('historiaClinica.id')">
              <span>Historia Clinica</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'historiaClinica.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="antecedenteClinico in antecedenteClinicos" :key="antecedenteClinico.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AntecedenteClinicoView', params: { antecedenteClinicoId: antecedenteClinico.id } }">{{
                antecedenteClinico.id
              }}</router-link>
            </td>
            <td>{{ antecedenteClinico.titulo }}</td>
            <td>{{ antecedenteClinico.descripcion }}</td>
            <td>{{ antecedenteClinico.fechaRegistro }}</td>
            <td>{{ antecedenteClinico.observaciones }}</td>
            <td>{{ antecedenteClinico.activo }}</td>
            <td>{{ antecedenteClinico.fechaAlta }}</td>
            <td>{{ antecedenteClinico.fechaBaja }}</td>
            <td>
              <div v-if="antecedenteClinico.historiaClinica">
                <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: antecedenteClinico.historiaClinica.id } }">{{
                  antecedenteClinico.historiaClinica.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AntecedenteClinicoView', params: { antecedenteClinicoId: antecedenteClinico.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'AntecedenteClinicoEdit', params: { antecedenteClinicoId: antecedenteClinico.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(antecedenteClinico)"
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
        <span id="hospitalApp.antecedenteClinico.delete.question" data-cy="antecedenteClinicoDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-antecedenteClinico-heading">¿Seguro que quiere eliminar Antecedente Clinico {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-antecedenteClinico"
            data-cy="entityConfirmDeleteButton"
            @click="removeAntecedenteClinico"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="antecedenteClinicos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./antecedente-clinico.component.ts"></script>
