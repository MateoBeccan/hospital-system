<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.turnoLaboral.home.createOrEditLabel" data-cy="TurnoLaboralCreateUpdateHeading">Crear o editar Turno Laboral</h2>
        <div>
          <div class="mb-3" v-if="turnoLaboral.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="turnoLaboral.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="turno-laboral-codigo"
              data-cy="codigo"
              :class="{ valid: !v$.codigo.$invalid, invalid: v$.codigo.$invalid }"
              v-model="v$.codigo.$model"
              required
            />
            <div v-if="v$.codigo.$anyDirty && v$.codigo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="turno-laboral-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Hora Inicio</label>
            <input
              type="text"
              class="form-control"
              name="horaInicio"
              id="turno-laboral-horaInicio"
              data-cy="horaInicio"
              :class="{ valid: !v$.horaInicio.$invalid, invalid: v$.horaInicio.$invalid }"
              v-model="v$.horaInicio.$model"
              required
            />
            <div v-if="v$.horaInicio.$anyDirty && v$.horaInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.horaInicio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Hora Fin</label>
            <input
              type="text"
              class="form-control"
              name="horaFin"
              id="turno-laboral-horaFin"
              data-cy="horaFin"
              :class="{ valid: !v$.horaFin.$invalid, invalid: v$.horaFin.$invalid }"
              v-model="v$.horaFin.$model"
              required
            />
            <div v-if="v$.horaFin.$anyDirty && v$.horaFin.$invalid">
              <small class="form-text text-danger" v-for="error of v$.horaFin.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="turno-laboral-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
            <div v-if="v$.descripcion.$anyDirty && v$.descripcion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.descripcion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="turno-laboral-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
              required
            />
            <div v-if="v$.activo.$anyDirty && v$.activo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="turno-laboral-fechaAlta"
                  v-model="v$.fechaAlta.$model"
                  name="fechaAlta"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="turno-laboral-fechaAlta"
                data-cy="fechaAlta"
                type="text"
                class="form-control"
                name="fechaAlta"
                :class="{ valid: !v$.fechaAlta.$invalid, invalid: v$.fechaAlta.$invalid }"
                v-model="v$.fechaAlta.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaAlta.$anyDirty && v$.fechaAlta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaAlta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno-laboral">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="turno-laboral-fechaBaja"
                  v-model="v$.fechaBaja.$model"
                  name="fechaBaja"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="turno-laboral-fechaBaja"
                data-cy="fechaBaja"
                type="text"
                class="form-control"
                name="fechaBaja"
                :class="{ valid: !v$.fechaBaja.$invalid, invalid: v$.fechaBaja.$invalid }"
                v-model="v$.fechaBaja.$model"
              />
            </b-input-group>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./turno-laboral-update.component.ts"></script>
