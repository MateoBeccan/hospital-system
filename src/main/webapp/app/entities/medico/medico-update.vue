<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.medico.home.createOrEditLabel" data-cy="MedicoCreateUpdateHeading">Crear o editar Medico</h2>
        <div>
          <div class="mb-3" v-if="medico.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="medico.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="medico">Matricula</label>
            <input
              type="text"
              class="form-control"
              name="matricula"
              id="medico-matricula"
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
            <label class="form-control-label" for="medico">Fecha Matriculacion</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="medico-fechaMatriculacion"
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
                id="medico-fechaMatriculacion"
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
            <label class="form-control-label" for="medico">Firma Digital</label>
            <input
              type="text"
              class="form-control"
              name="firmaDigital"
              id="medico-firmaDigital"
              data-cy="firmaDigital"
              :class="{ valid: !v$.firmaDigital.$invalid, invalid: v$.firmaDigital.$invalid }"
              v-model="v$.firmaDigital.$model"
            />
            <div v-if="v$.firmaDigital.$anyDirty && v$.firmaDigital.$invalid">
              <small class="form-text text-danger" v-for="error of v$.firmaDigital.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="medico">Atiende Consultorio</label>
            <input
              type="checkbox"
              class="form-check"
              name="atiendeConsultorio"
              id="medico-atiendeConsultorio"
              data-cy="atiendeConsultorio"
              :class="{ valid: !v$.atiendeConsultorio.$invalid, invalid: v$.atiendeConsultorio.$invalid }"
              v-model="v$.atiendeConsultorio.$model"
              required
            />
            <div v-if="v$.atiendeConsultorio.$anyDirty && v$.atiendeConsultorio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.atiendeConsultorio.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="medico">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="medico-activo"
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
            <label class="form-control-label" for="medico">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="medico-fechaAlta"
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
                id="medico-fechaAlta"
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
            <label class="form-control-label" for="medico">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="medico-fechaBaja"
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
                id="medico-fechaBaja"
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
            <label class="form-control-label" for="medico">Empleado</label>
            <select class="form-control" id="medico-empleado" data-cy="empleado" name="empleado" v-model="medico.empleado" required>
              <option v-if="!medico.empleado" :value="null" selected></option>
              <option
                :value="medico.empleado && empleadoOption.id === medico.empleado.id ? medico.empleado : empleadoOption"
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
            <label class="form-control-label" for="medico">Especialidad</label>
            <select
              class="form-control"
              id="medico-especialidad"
              data-cy="especialidad"
              name="especialidad"
              v-model="medico.especialidad"
              required
            >
              <option v-if="!medico.especialidad" :value="null" selected></option>
              <option
                :value="medico.especialidad && especialidadOption.id === medico.especialidad.id ? medico.especialidad : especialidadOption"
                v-for="especialidadOption in especialidads"
                :key="especialidadOption.id"
              >
                {{ especialidadOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.especialidad.$anyDirty && v$.especialidad.$invalid">
            <small class="form-text text-danger" v-for="error of v$.especialidad.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./medico-update.component.ts"></script>
