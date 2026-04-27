<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="tratamiento">
        <h2 class="jh-entity-heading" data-cy="tratamientoDetailsHeading"><span>TratamientoTratamiento</span> {{ tratamiento.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ tratamiento.codigo }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ tratamiento.descripcion }}</span>
          </dd>
          <dt>
            <span>Fecha Inicio</span>
          </dt>
          <dd>
            <span>{{ tratamiento.fechaInicio }}</span>
          </dd>
          <dt>
            <span>Fecha Fin</span>
          </dt>
          <dd>
            <span>{{ tratamiento.fechaFin }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ tratamiento.observaciones }}</span>
          </dd>
          <dt>
            <span>Fecha Proxima Revision</span>
          </dt>
          <dd>
            <span>{{ tratamiento.fechaProximaRevision }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ tratamiento.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ tratamiento.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ tratamiento.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Diagnostico</span>
          </dt>
          <dd>
            <div v-if="tratamiento.diagnostico">
              <router-link :to="{ name: 'DiagnosticoView', params: { diagnosticoId: tratamiento.diagnostico.id } }">{{
                tratamiento.diagnostico.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Estado Tratamiento</span>
          </dt>
          <dd>
            <div v-if="tratamiento.estadoTratamiento">
              <router-link :to="{ name: 'EstadoTratamientoView', params: { estadoTratamientoId: tratamiento.estadoTratamiento.id } }">{{
                tratamiento.estadoTratamiento.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="tratamiento.id"
          :to="{ name: 'TratamientoEdit', params: { tratamientoId: tratamiento.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tratamiento-details.component.ts"></script>
