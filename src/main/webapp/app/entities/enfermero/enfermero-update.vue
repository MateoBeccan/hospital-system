<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.enfermero.home.createOrEditLabel" data-cy="EnfermeroCreateUpdateHeading">Crear o editar Enfermero</h2>
        <div>
          <div class="mb-3" v-if="enfermero.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="enfermero.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="enfermero">Matricula</label>
            <input
              type="text"
              class="form-control"
              name="matricula"
              id="enfermero-matricula"
              data-cy="matricula"
              :class="{ valid: !v$.matricula.$invalid, invalid: v$.matricula.$invalid }"
              v-model="v$.matricula.$model"
              required
            />
            <div v-if="v$.matricula.$anyDirty && v$.matricula.$invalid">
              <small class="form-text text-danger" v-for="error of v$.matricula.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="enfermero">Fecha Matriculacion</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="enfermero-fechaMatriculacion"
                  v-model="v$.fechaMatriculacion.$model"
                  name="fechaMatriculacion"
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
                id="enfermero-fechaMatriculacion"
                data-cy="fechaMatriculacion"
                type="text"
                class="form-control"
                name="fechaMatriculacion"
                :class="{ valid: !v$.fechaMatriculacion.$invalid, invalid: v$.fechaMatriculacion.$invalid }"
                v-model="v$.fechaMatriculacion.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="enfermero">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="enfermero-activo"
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
            <label class="form-control-label" for="enfermero">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="enfermero-fechaAlta"
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
                id="enfermero-fechaAlta"
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
            <label class="form-control-label" for="enfermero">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="enfermero-fechaBaja"
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
                id="enfermero-fechaBaja"
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
            <label class="form-control-label" for="enfermero">Empleado</label>
            <select class="form-control" id="enfermero-empleado" data-cy="empleado" name="empleado" v-model="enfermero.empleado" required>
              <option v-if="!enfermero.empleado" :value="null" selected></option>
              <option
                :value="enfermero.empleado && empleadoOption.id === enfermero.empleado.id ? enfermero.empleado : empleadoOption"
                v-for="empleadoOption in empleados"
                :key="empleadoOption.id"
              >
                {{ empleadoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.empleado.$anyDirty && v$.empleado.$invalid">
            <small class="form-text text-danger" v-for="error of v$.empleado.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="enfermero">Turno Laboral</label>
            <select
              class="form-control"
              id="enfermero-turnoLaboral"
              data-cy="turnoLaboral"
              name="turnoLaboral"
              v-model="enfermero.turnoLaboral"
            >
              <option :value="null"></option>
              <option
                :value="
                  enfermero.turnoLaboral && turnoLaboralOption.id === enfermero.turnoLaboral.id
                    ? enfermero.turnoLaboral
                    : turnoLaboralOption
                "
                v-for="turnoLaboralOption in turnoLaborals"
                :key="turnoLaboralOption.id"
              >
                {{ turnoLaboralOption.id }}
              </option>
            </select>
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
<script lang="ts" src="./enfermero-update.component.ts"></script>
