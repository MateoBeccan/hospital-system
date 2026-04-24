<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="contactoEmergencia">
        <h2 class="jh-entity-heading" data-cy="contactoEmergenciaDetailsHeading">
          <span>Contacto EmergenciaContactoEmergencia</span> {{ contactoEmergencia.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.nombre }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.telefono }}</span>
          </dd>
          <dt>
            <span>Parentesco</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.parentesco }}</span>
          </dd>
          <dt>
            <span>Observaciones</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.observaciones }}</span>
          </dd>
          <dt>
            <span>Prioridad</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.prioridad }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ contactoEmergencia.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Persona</span>
          </dt>
          <dd>
            <div v-if="contactoEmergencia.persona">
              <router-link :to="{ name: 'PersonaView', params: { personaId: contactoEmergencia.persona.id } }">{{
                contactoEmergencia.persona.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="contactoEmergencia.id"
          :to="{ name: 'ContactoEmergenciaEdit', params: { contactoEmergenciaId: contactoEmergencia.id } }"
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

<script lang="ts" src="./contacto-emergencia-details.component.ts"></script>
