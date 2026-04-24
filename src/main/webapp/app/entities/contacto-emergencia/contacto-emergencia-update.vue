<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.contactoEmergencia.home.createOrEditLabel" data-cy="ContactoEmergenciaCreateUpdateHeading">
          Crear o editar Contacto Emergencia
        </h2>
        <div>
          <div class="mb-3" v-if="contactoEmergencia.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="contactoEmergencia.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="contacto-emergencia-nombre"
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
            <label class="form-control-label" for="contacto-emergencia">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="contacto-emergencia-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
              required
            />
            <div v-if="v$.telefono.$anyDirty && v$.telefono.$invalid">
              <small class="form-text text-danger" v-for="error of v$.telefono.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Parentesco</label>
            <input
              type="text"
              class="form-control"
              name="parentesco"
              id="contacto-emergencia-parentesco"
              data-cy="parentesco"
              :class="{ valid: !v$.parentesco.$invalid, invalid: v$.parentesco.$invalid }"
              v-model="v$.parentesco.$model"
            />
            <div v-if="v$.parentesco.$anyDirty && v$.parentesco.$invalid">
              <small class="form-text text-danger" v-for="error of v$.parentesco.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Observaciones</label>
            <input
              type="text"
              class="form-control"
              name="observaciones"
              id="contacto-emergencia-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            />
            <div v-if="v$.observaciones.$anyDirty && v$.observaciones.$invalid">
              <small class="form-text text-danger" v-for="error of v$.observaciones.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Prioridad</label>
            <input
              type="number"
              class="form-control"
              name="prioridad"
              id="contacto-emergencia-prioridad"
              data-cy="prioridad"
              :class="{ valid: !v$.prioridad.$invalid, invalid: v$.prioridad.$invalid }"
              v-model.number="v$.prioridad.$model"
              required
            />
            <div v-if="v$.prioridad.$anyDirty && v$.prioridad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.prioridad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="contacto-emergencia-activo"
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
            <label class="form-control-label" for="contacto-emergencia">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="contacto-emergencia-fechaAlta"
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
                id="contacto-emergencia-fechaAlta"
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
            <label class="form-control-label" for="contacto-emergencia">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="contacto-emergencia-fechaBaja"
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
                id="contacto-emergencia-fechaBaja"
                data-cy="fechaBaja"
                type="text"
                class="form-control"
                name="fechaBaja"
                :class="{ valid: !v$.fechaBaja.$invalid, invalid: v$.fechaBaja.$invalid }"
                v-model="v$.fechaBaja.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="contacto-emergencia">Persona</label>
            <select
              class="form-control"
              id="contacto-emergencia-persona"
              data-cy="persona"
              name="persona"
              v-model="contactoEmergencia.persona"
              required
            >
              <option v-if="!contactoEmergencia.persona" :value="null" selected></option>
              <option
                :value="
                  contactoEmergencia.persona && personaOption.id === contactoEmergencia.persona.id
                    ? contactoEmergencia.persona
                    : personaOption
                "
                v-for="personaOption in personas"
                :key="personaOption.id"
              >
                {{ personaOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.persona.$anyDirty && v$.persona.$invalid">
            <small class="form-text text-danger" v-for="error of v$.persona.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./contacto-emergencia-update.component.ts"></script>
