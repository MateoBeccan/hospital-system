<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="consulta">
        <h2 class="jh-entity-heading" data-cy="consultaDetailsHeading"><span>ConsultaConsulta</span> {{ consulta.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ consulta.codigo }}</span>
          </dd>
          <dt>
            <span>Fecha Hora Inicio</span>
          </dt>
          <dd>
            <span v-if="consulta.fechaHoraInicio">{{ formatDateLong(consulta.fechaHoraInicio) }}</span>
          </dd>
          <dt>
            <span>Fecha Hora Fin</span>
          </dt>
          <dd>
            <span v-if="consulta.fechaHoraFin">{{ formatDateLong(consulta.fechaHoraFin) }}</span>
          </dd>
          <dt>
            <span>Sintomas</span>
          </dt>
          <dd>
            <span>{{ consulta.sintomas }}</span>
          </dd>
          <dt>
            <span>Motivo Consulta</span>
          </dt>
          <dd>
            <span>{{ consulta.motivoConsulta }}</span>
          </dd>
          <dt>
            <span>Examen Fisico</span>
          </dt>
          <dd>
            <span>{{ consulta.examenFisico }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ consulta.observaciones }}</span>
          </dd>
          <dt>
            <span>Indicaciones</span>
          </dt>
          <dd>
            <span>{{ consulta.indicaciones }}</span>
          </dd>
          <dt>
            <span>Activa</span>
          </dt>
          <dd>
            <span>{{ consulta.activa }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ consulta.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ consulta.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Turno</span>
          </dt>
          <dd>
            <div v-if="consulta.turno">
              <router-link :to="{ name: 'TurnoView', params: { turnoId: consulta.turno.id } }">{{ consulta.turno.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Paciente</span>
          </dt>
          <dd>
            <div v-if="consulta.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: consulta.paciente.id } }">{{
                consulta.paciente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Medico</span>
          </dt>
          <dd>
            <div v-if="consulta.medico">
              <router-link :to="{ name: 'MedicoView', params: { medicoId: consulta.medico.id } }">{{ consulta.medico.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Historia Clinica</span>
          </dt>
          <dd>
            <div v-if="consulta.historiaClinica">
              <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: consulta.historiaClinica.id } }">{{
                consulta.historiaClinica.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="consulta.id" :to="{ name: 'ConsultaEdit', params: { consultaId: consulta.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./consulta-details.component.ts"></script>
