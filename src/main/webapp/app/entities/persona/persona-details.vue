<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="persona">
        <h2 class="jh-entity-heading" data-cy="personaDetailsHeading"><span>PersonaPersona</span> {{ persona.id }}</h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ persona.nombre }}</span>
          </dd>
          <dt>
            <span>Apellido</span>
          </dt>
          <dd>
            <span>{{ persona.apellido }}</span>
          </dd>
          <dt>
            <span>Nro Documento</span>
          </dt>
          <dd>
            <span>{{ persona.nroDocumento }}</span>
          </dd>
          <dt>
            <span>Fecha Nacimiento</span>
          </dt>
          <dd>
            <span>{{ persona.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ persona.telefono }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ persona.email }}</span>
          </dd>
          <dt>
            <span>Direccion</span>
          </dt>
          <dd>
            <span>{{ persona.direccion }}</span>
          </dd>
          <dt>
            <span>Activo</span>
          </dt>
          <dd>
            <span>{{ persona.activo }}</span>
          </dd>
          <dt>
            <span>Fecha Alta</span>
          </dt>
          <dd>
            <span>{{ persona.fechaAlta }}</span>
          </dd>
          <dt>
            <span>Fecha Baja</span>
          </dt>
          <dd>
            <span>{{ persona.fechaBaja }}</span>
          </dd>
          <dt>
            <span>Tipo Documento</span>
          </dt>
          <dd>
            <div v-if="persona.tipoDocumento">
              <router-link :to="{ name: 'TipoDocumentoView', params: { tipoDocumentoId: persona.tipoDocumento.id } }">{{
                persona.tipoDocumento.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Sexo</span>
          </dt>
          <dd>
            <div v-if="persona.sexo">
              <router-link :to="{ name: 'SexoView', params: { sexoId: persona.sexo.id } }">{{ persona.sexo.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Ciudad</span>
          </dt>
          <dd>
            <div v-if="persona.ciudad">
              <router-link :to="{ name: 'CiudadView', params: { ciudadId: persona.ciudad.id } }">{{ persona.ciudad.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="persona.id" :to="{ name: 'PersonaEdit', params: { personaId: persona.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./persona-details.component.ts"></script>
