<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="diagnostico">
        <h2 class="jh-entity-heading" data-cy="diagnosticoDetailsHeading"><span>DiagnosticoDiagnostico</span> {{ diagnostico.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ diagnostico.codigo }}</span>
          </dd>
          <dt>
            <span>Fecha Diagnostico</span>
          </dt>
          <dd>
            <span>{{ diagnostico.fechaDiagnostico }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ diagnostico.descripcion }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ diagnostico.observaciones }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ diagnostico.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Resolucion</span>
          </dt>
          <dd>
            <span>{{ diagnostico.fechaResolucion }}</span>
          </dd>
          <dt>
            <span>Es Principal</span>
          </dt>
          <dd>
            <span>{{ diagnostico.esPrincipal }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ diagnostico.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ diagnostico.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Consulta</span>
          </dt>
          <dd>
            <div v-if="diagnostico.consulta">
              <router-link :to="{ name: 'ConsultaView', params: { consultaId: diagnostico.consulta.id } }">{{
                diagnostico.consulta.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Paciente</span>
          </dt>
          <dd>
            <div v-if="diagnostico.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: diagnostico.paciente.id } }">{{
                diagnostico.paciente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Medico</span>
          </dt>
          <dd>
            <div v-if="diagnostico.medico">
              <router-link :to="{ name: 'MedicoView', params: { medicoId: diagnostico.medico.id } }">{{
                diagnostico.medico.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Tipo Diagnostico</span>
          </dt>
          <dd>
            <div v-if="diagnostico.tipoDiagnostico">
              <router-link :to="{ name: 'TipoDiagnosticoView', params: { tipoDiagnosticoId: diagnostico.tipoDiagnostico.id } }">{{
                diagnostico.tipoDiagnostico.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Estado Diagnostico</span>
          </dt>
          <dd>
            <div v-if="diagnostico.estadoDiagnostico">
              <router-link :to="{ name: 'EstadoDiagnosticoView', params: { estadoDiagnosticoId: diagnostico.estadoDiagnostico.id } }">{{
                diagnostico.estadoDiagnostico.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="diagnostico.id"
          :to="{ name: 'DiagnosticoEdit', params: { diagnosticoId: diagnostico.id } }"
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

<script lang="ts" src="./diagnostico-details.component.ts"></script>
