<template>
  <div>
    <h2 id="page-heading" data-cy="SignosVitalesHeading">
      <span id="signos-vitales">Signos Vitales</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'SignosVitalesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-signos-vitales"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Signos Vitales</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && signosVitaleses?.length === 0">
      <span>Ningún Signos Vitales encontrado</span>
    </div>
    <div class="table-responsive" v-if="signosVitaleses?.length > 0">
      <table class="table table-striped" aria-describedby="signosVitaleses">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechaHoraRegistro')">
              <span>Fecha Hora Registro</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaHoraRegistro'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('peso')">
              <span>Peso</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'peso'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('talla')">
              <span>Talla</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'talla'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('temperatura')">
              <span>Temperatura</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'temperatura'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('presionArterial')">
              <span>Presion Arterial</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'presionArterial'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('frecuenciaCardiaca')">
              <span>Frecuencia Cardiaca</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'frecuenciaCardiaca'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('frecuenciaRespiratoria')">
              <span>Frecuencia Respiratoria</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'frecuenciaRespiratoria'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('saturacionOxigeno')">
              <span>Saturacion Oxigeno</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'saturacionOxigeno'"></jhi-sort-indicator>
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
            <th scope="col" @click="changeOrder('consulta.id')">
              <span>Consulta</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'consulta.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="signosVitales in signosVitaleses" :key="signosVitales.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }">{{
                signosVitales.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(signosVitales.fechaHoraRegistro) || '' }}</td>
            <td>{{ signosVitales.peso }}</td>
            <td>{{ signosVitales.talla }}</td>
            <td>{{ signosVitales.temperatura }}</td>
            <td>{{ signosVitales.presionArterial }}</td>
            <td>{{ signosVitales.frecuenciaCardiaca }}</td>
            <td>{{ signosVitales.frecuenciaRespiratoria }}</td>
            <td>{{ signosVitales.saturacionOxigeno }}</td>
            <td>{{ signosVitales.observaciones }}</td>
            <td>{{ signosVitales.activo }}</td>
            <td>{{ signosVitales.fechaAlta }}</td>
            <td>{{ signosVitales.fechaBaja }}</td>
            <td>
              <div v-if="signosVitales.consulta">
                <router-link :to="{ name: 'ConsultaView', params: { consultaId: signosVitales.consulta.id } }">{{
                  signosVitales.consulta.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(signosVitales)"
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
        <span id="hospitalApp.signosVitales.delete.question" data-cy="signosVitalesDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-signosVitales-heading">¿Seguro que quiere eliminar Signos Vitales {{ removeId }}?</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-signosVitales"
            data-cy="entityConfirmDeleteButton"
            @click="removeSignosVitales"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="signosVitaleses?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales.component.ts"></script>
