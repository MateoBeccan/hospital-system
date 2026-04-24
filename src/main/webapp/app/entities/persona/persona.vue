<template>
  <div>
    <h2 id="page-heading" data-cy="PersonaHeading">
      <span id="persona">Personas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'PersonaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-persona"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Persona</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && personas?.length === 0">
      <span>Ningún Personas encontrado</span>
    </div>
    <div class="table-responsive" v-if="personas?.length > 0">
      <table class="table table-striped" aria-describedby="personas">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('apellido')">
              <span>Apellido</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'apellido'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('nroDocumento')">
              <span>Nro Documento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nroDocumento'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaNacimiento')">
              <span>Fecha Nacimiento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaNacimiento'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('telefono')">
              <span>Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefono'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('email')">
              <span>Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('direccion')">
              <span>Direccion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'direccion'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('tipoDocumento.id')">
              <span>Tipo Documento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoDocumento.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sexo.id')">
              <span>Sexo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sexo.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('ciudad.id')">
              <span>Ciudad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ciudad.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="persona in personas" :key="persona.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PersonaView', params: { personaId: persona.id } }">{{ persona.id }}</router-link>
            </td>
            <td>{{ persona.nombre }}</td>
            <td>{{ persona.apellido }}</td>
            <td>{{ persona.nroDocumento }}</td>
            <td>{{ persona.fechaNacimiento }}</td>
            <td>{{ persona.telefono }}</td>
            <td>{{ persona.email }}</td>
            <td>{{ persona.direccion }}</td>
            <td>{{ persona.activo }}</td>
            <td>{{ persona.fechaAlta }}</td>
            <td>{{ persona.fechaBaja }}</td>
            <td>
              <div v-if="persona.tipoDocumento">
                <router-link :to="{ name: 'TipoDocumentoView', params: { tipoDocumentoId: persona.tipoDocumento.id } }">{{
                  persona.tipoDocumento.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="persona.sexo">
                <router-link :to="{ name: 'SexoView', params: { sexoId: persona.sexo.id } }">{{ persona.sexo.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="persona.ciudad">
                <router-link :to="{ name: 'CiudadView', params: { ciudadId: persona.ciudad.id } }">{{ persona.ciudad.id }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PersonaView', params: { personaId: persona.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PersonaEdit', params: { personaId: persona.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(persona)"
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
        <span id="hospitalApp.persona.delete.question" data-cy="personaDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-persona-heading">¿Seguro que quiere eliminar Persona {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-persona"
            data-cy="entityConfirmDeleteButton"
            @click="removePersona"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="personas?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./persona.component.ts"></script>
