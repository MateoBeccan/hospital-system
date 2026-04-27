<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="signosVitales">
        <h2 class="jh-entity-heading" data-cy="signosVitalesDetailsHeading">
          <span>Signos VitalesSignosVitales</span> {{ signosVitales.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Fecha Hora Registro</span>
          </dt>
          <dd>
            <span v-if="signosVitales.fechaHoraRegistro">{{ formatDateLong(signosVitales.fechaHoraRegistro) }}</span>
          </dd>
          <dt>
            <span>Peso</span>
          </dt>
          <dd>
            <span>{{ signosVitales.peso }}</span>
          </dd>
          <dt>
            <span>Talla</span>
          </dt>
          <dd>
            <span>{{ signosVitales.talla }}</span>
          </dd>
          <dt>
            <span>Temperatura</span>
          </dt>
          <dd>
            <span>{{ signosVitales.temperatura }}</span>
          </dd>
          <dt>
            <span>Presion Arterial</span>
          </dt>
          <dd>
            <span>{{ signosVitales.presionArterial }}</span>
          </dd>
          <dt>
            <span>Frecuencia Cardiaca</span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaCardiaca }}</span>
          </dd>
          <dt>
            <span>Frecuencia Respiratoria</span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaRespiratoria }}</span>
          </dd>
          <dt>
            <span>Saturacion Oxigeno</span>
          </dt>
          <dd>
            <span>{{ signosVitales.saturacionOxigeno }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ signosVitales.observaciones }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ signosVitales.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ signosVitales.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ signosVitales.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Consulta</span>
          </dt>
          <dd>
            <div v-if="signosVitales.consulta">
              <router-link :to="{ name: 'ConsultaView', params: { consultaId: signosVitales.consulta.id } }">{{
                signosVitales.consulta.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="signosVitales.id"
          :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
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

<script lang="ts" src="./signos-vitales-details.component.ts"></script>
