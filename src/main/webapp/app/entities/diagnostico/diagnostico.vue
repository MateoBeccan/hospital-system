<template>
  <div>
    <h2 id="page-heading" data-cy="DiagnosticoHeading">
      <span id="diagnostico">Diagnosticos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'DiagnosticoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-diagnostico"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Diagnostico</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && diagnosticos?.length === 0">
      <span>Ningún Diagnosticos encontrado</span>
    </div>
    <div class="table-responsive" v-if="diagnosticos?.length > 0">
      <table class="table table-striped" aria-describedby="diagnosticos">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaDiagnostico')">
              <span>Fecha Diagnostico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaDiagnostico'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('observaciones')">
              <span>Observaciones</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observaciones'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('activo')">
              <span>Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaResolucion')">
              <span>Fecha Resolucion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaResolucion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('esPrincipal')">
              <span>Es Principal</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'esPrincipal'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaAlta')">
              <span>Fecha Alta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaAlta'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaBaja')">
              <span>Fecha Baja</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaBaja'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('consulta.id')">
              <span>Consulta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'consulta.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('paciente.id')">
              <span>Paciente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('medico.id')">
              <span>Medico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'medico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('tipoDiagnostico.id')">
              <span>Tipo Diagnostico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoDiagnostico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('estadoDiagnostico.id')">
              <span>Estado Diagnostico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estadoDiagnostico.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="diagnostico in diagnosticos" :key="diagnostico.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DiagnosticoView', params: { diagnosticoId: diagnostico.id } }">{{ diagnostico.id }}</router-link>
            </td>
            <td>{{ diagnostico.codigo }}</td>
            <td>{{ diagnostico.fechaDiagnostico }}</td>
            <td>{{ diagnostico.descripcion }}</td>
            <td>{{ diagnostico.observaciones }}</td>
            <td>{{ diagnostico.activo }}</td>
            <td>{{ diagnostico.fechaResolucion }}</td>
            <td>{{ diagnostico.esPrincipal }}</td>
            <td>{{ diagnostico.fechaAlta }}</td>
            <td>{{ diagnostico.fechaBaja }}</td>
            <td>
              <div v-if="diagnostico.consulta">
                <router-link :to="{ name: 'ConsultaView', params: { consultaId: diagnostico.consulta.id } }">{{
                  diagnostico.consulta.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="diagnostico.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: diagnostico.paciente.id } }">{{
                  diagnostico.paciente.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="diagnostico.medico">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: diagnostico.medico.id } }">{{
                  diagnostico.medico.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="diagnostico.tipoDiagnostico">
                <router-link :to="{ name: 'TipoDiagnosticoView', params: { tipoDiagnosticoId: diagnostico.tipoDiagnostico.id } }">{{
                  diagnostico.tipoDiagnostico.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="diagnostico.estadoDiagnostico">
                <router-link :to="{ name: 'EstadoDiagnosticoView', params: { estadoDiagnosticoId: diagnostico.estadoDiagnostico.id } }">{{
                  diagnostico.estadoDiagnostico.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'DiagnosticoView', params: { diagnosticoId: diagnostico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DiagnosticoEdit', params: { diagnosticoId: diagnostico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(diagnostico)"
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
        <span id="hospitalApp.diagnostico.delete.question" data-cy="diagnosticoDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-diagnostico-heading">¿Seguro que quiere eliminar Diagnostico {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-diagnostico"
            data-cy="entityConfirmDeleteButton"
            @click="removeDiagnostico"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="diagnosticos?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./diagnostico.component.ts"></script>
