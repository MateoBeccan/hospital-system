<template>
  <div>
    <h2 id="page-heading" data-cy="ContactoEmergenciaHeading">
      <span id="contacto-emergencia">Contacto Emergencias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'ContactoEmergenciaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-contacto-emergencia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Contacto Emergencia</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && contactoEmergencias?.length === 0">
      <span>Ningún Contacto Emergencias encontrado</span>
    </div>
    <div class="table-responsive" v-if="contactoEmergencias?.length > 0">
      <table class="table table-striped" aria-describedby="contactoEmergencias">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('telefono')">
              <span>Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefono'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('parentesco')">
              <span>Parentesco</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'parentesco'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('prioridad')">
              <span>Prioridad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'prioridad'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('persona.id')">
              <span>Persona</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'persona.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="contactoEmergencia in contactoEmergencias" :key="contactoEmergencia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ContactoEmergenciaView', params: { contactoEmergenciaId: contactoEmergencia.id } }">{{
                contactoEmergencia.id
              }}</router-link>
            </td>
            <td>{{ contactoEmergencia.nombre }}</td>
            <td>{{ contactoEmergencia.telefono }}</td>
            <td>{{ contactoEmergencia.parentesco }}</td>
            <td>{{ contactoEmergencia.observaciones }}</td>
            <td>{{ contactoEmergencia.prioridad }}</td>
            <td>{{ contactoEmergencia.activo }}</td>
            <td>{{ contactoEmergencia.fechaAlta }}</td>
            <td>{{ contactoEmergencia.fechaBaja }}</td>
            <td>
              <div v-if="contactoEmergencia.persona">
                <router-link :to="{ name: 'PersonaView', params: { personaId: contactoEmergencia.persona.id } }">{{
                  contactoEmergencia.persona.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ContactoEmergenciaView', params: { contactoEmergenciaId: contactoEmergencia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ContactoEmergenciaEdit', params: { contactoEmergenciaId: contactoEmergencia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(contactoEmergencia)"
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
        <span id="hospitalApp.contactoEmergencia.delete.question" data-cy="contactoEmergenciaDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-contactoEmergencia-heading">¿Seguro que quiere eliminar Contacto Emergencia {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-contactoEmergencia"
            data-cy="entityConfirmDeleteButton"
            @click="removeContactoEmergencia"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="contactoEmergencias?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./contacto-emergencia.component.ts"></script>
