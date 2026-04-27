<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="turno">
        <h2 class="jh-entity-heading" data-cy="turnoDetailsHeading"><span>TurnoTurno</span> {{ turno.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ turno.codigo }}</span>
          </dd>
          <dt>
            <span>Fecha Hora</span>
          </dt>
          <dd>
            <span v-if="turno.fechaHora">{{ formatDateLong(turno.fechaHora) }}</span>
          </dd>
          <dt>
            <span>Duracion Minutos</span>
          </dt>
          <dd>
            <span>{{ turno.duracionMinutos }}</span>
          </dd>
          <dt>
            <span>Motivo Consulta</span>
          </dt>
          <dd>
            <span>{{ turno.motivoConsulta }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ turno.observaciones }}</span>
          </dd>
          <dt>
            <span>Fecha Creacion</span>
          </dt>
          <dd>
            <span v-if="turno.fechaCreacion">{{ formatDateLong(turno.fechaCreacion) }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ turno.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ turno.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ turno.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Paciente</span>
          </dt>
          <dd>
            <div v-if="turno.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: turno.paciente.id } }">{{ turno.paciente.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Medico</span>
          </dt>
          <dd>
            <div v-if="turno.medico">
              <router-link :to="{ name: 'MedicoView', params: { medicoId: turno.medico.id } }">{{ turno.medico.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Especialidad</span>
          </dt>
          <dd>
            <div v-if="turno.especialidad">
              <router-link :to="{ name: 'EspecialidadView', params: { especialidadId: turno.especialidad.id } }">{{
                turno.especialidad.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Estado Turno</span>
          </dt>
          <dd>
            <div v-if="turno.estadoTurno">
              <router-link :to="{ name: 'EstadoTurnoView', params: { estadoTurnoId: turno.estadoTurno.id } }">{{
                turno.estadoTurno.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Canal Solicitud</span>
          </dt>
          <dd>
            <div v-if="turno.canalSolicitud">
              <router-link :to="{ name: 'CanalSolicitudView', params: { canalSolicitudId: turno.canalSolicitud.id } }">{{
                turno.canalSolicitud.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="turno.id" :to="{ name: 'TurnoEdit', params: { turnoId: turno.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./turno-details.component.ts"></script>
