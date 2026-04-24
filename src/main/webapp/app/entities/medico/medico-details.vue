<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="medico">
        <h2 class="jh-entity-heading" data-cy="medicoDetailsHeading"><span>MedicoMedico</span> {{ medico.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Matricula</span>
          </dt>
          <dd>
            <span>{{ medico.matricula }}</span>
          </dd>
          <dt>
            <span>Fecha Matriculacion</span>
          </dt>
          <dd>
            <span>{{ medico.fechaMatriculacion }}</span>
          </dd>
          <dt>
            <span>Firma Digital</span>
          </dt>
          <dd>
            <span>{{ medico.firmaDigital }}</span>
          </dd>
          <dt>
            <span>Atiende Consultorio</span>
          </dt>
          <dd>
            <span>{{ medico.atiendeConsultorio }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ medico.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ medico.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ medico.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Empleado</span>
          </dt>
          <dd>
            <div v-if="medico.empleado">
              <router-link :to="{ name: 'EmpleadoView', params: { empleadoId: medico.empleado.id } }">{{ medico.empleado.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Especialidad</span>
          </dt>
          <dd>
            <div v-if="medico.especialidad">
              <router-link :to="{ name: 'EspecialidadView', params: { especialidadId: medico.especialidad.id } }">{{
                medico.especialidad.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="medico.id" :to="{ name: 'MedicoEdit', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medico-details.component.ts"></script>
