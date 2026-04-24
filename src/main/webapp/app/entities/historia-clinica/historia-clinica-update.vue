<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.historiaClinica.home.createOrEditLabel" data-cy="HistoriaClinicaCreateUpdateHeading">
          Crear o editar Historia Clinica
        </h2>
        <div>
          <div class="mb-3" v-if="historiaClinica.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="historiaClinica.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Numero</label>
            <input
              type="text"
              class="form-control"
              name="numero"
              id="historia-clinica-numero"
              data-cy="numero"
              :class="{ valid: !v$.numero.$invalid, invalid: v$.numero.$invalid }"
              v-model="v$.numero.$model"
              required
            />
            <div v-if="v$.numero.$anyDirty && v$.numero.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numero.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Fecha Apertura</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="historia-clinica-fechaApertura"
                  v-model="v$.fechaApertura.$model"
                  name="fechaApertura"
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
                id="historia-clinica-fechaApertura"
                data-cy="fechaApertura"
                type="text"
                class="form-control"
                name="fechaApertura"
                :class="{ valid: !v$.fechaApertura.$invalid, invalid: v$.fechaApertura.$invalid }"
                v-model="v$.fechaApertura.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaApertura.$anyDirty && v$.fechaApertura.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaApertura.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Fecha Ultima Actualizacion</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="historia-clinica-fechaUltimaActualizacion"
                  v-model="v$.fechaUltimaActualizacion.$model"
                  name="fechaUltimaActualizacion"
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
                id="historia-clinica-fechaUltimaActualizacion"
                data-cy="fechaUltimaActualizacion"
                type="text"
                class="form-control"
                name="fechaUltimaActualizacion"
                :class="{ valid: !v$.fechaUltimaActualizacion.$invalid, invalid: v$.fechaUltimaActualizacion.$invalid }"
                v-model="v$.fechaUltimaActualizacion.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Antecedentes Personales</label>
            <textarea
              class="form-control"
              name="antecedentesPersonales"
              id="historia-clinica-antecedentesPersonales"
              data-cy="antecedentesPersonales"
              :class="{ valid: !v$.antecedentesPersonales.$invalid, invalid: v$.antecedentesPersonales.$invalid }"
              v-model="v$.antecedentesPersonales.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Antecedentes Familiares</label>
            <textarea
              class="form-control"
              name="antecedentesFamiliares"
              id="historia-clinica-antecedentesFamiliares"
              data-cy="antecedentesFamiliares"
              :class="{ valid: !v$.antecedentesFamiliares.$invalid, invalid: v$.antecedentesFamiliares.$invalid }"
              v-model="v$.antecedentesFamiliares.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Enfermedades Previas</label>
            <textarea
              class="form-control"
              name="enfermedadesPrevias"
              id="historia-clinica-enfermedadesPrevias"
              data-cy="enfermedadesPrevias"
              :class="{ valid: !v$.enfermedadesPrevias.$invalid, invalid: v$.enfermedadesPrevias.$invalid }"
              v-model="v$.enfermedadesPrevias.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Cirugias Previas</label>
            <textarea
              class="form-control"
              name="cirugiasPrevias"
              id="historia-clinica-cirugiasPrevias"
              data-cy="cirugiasPrevias"
              :class="{ valid: !v$.cirugiasPrevias.$invalid, invalid: v$.cirugiasPrevias.$invalid }"
              v-model="v$.cirugiasPrevias.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Alergias</label>
            <textarea
              class="form-control"
              name="alergias"
              id="historia-clinica-alergias"
              data-cy="alergias"
              :class="{ valid: !v$.alergias.$invalid, invalid: v$.alergias.$invalid }"
              v-model="v$.alergias.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Medicacion Habitual</label>
            <textarea
              class="form-control"
              name="medicacionHabitual"
              id="historia-clinica-medicacionHabitual"
              data-cy="medicacionHabitual"
              :class="{ valid: !v$.medicacionHabitual.$invalid, invalid: v$.medicacionHabitual.$invalid }"
              v-model="v$.medicacionHabitual.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Habitos</label>
            <textarea
              class="form-control"
              name="habitos"
              id="historia-clinica-habitos"
              data-cy="habitos"
              :class="{ valid: !v$.habitos.$invalid, invalid: v$.habitos.$invalid }"
              v-model="v$.habitos.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Observaciones Generales</label>
            <textarea
              class="form-control"
              name="observacionesGenerales"
              id="historia-clinica-observacionesGenerales"
              data-cy="observacionesGenerales"
              :class="{ valid: !v$.observacionesGenerales.$invalid, invalid: v$.observacionesGenerales.$invalid }"
              v-model="v$.observacionesGenerales.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Activa</label>
            <input
              type="checkbox"
              class="form-check"
              name="activa"
              id="historia-clinica-activa"
              data-cy="activa"
              :class="{ valid: !v$.activa.$invalid, invalid: v$.activa.$invalid }"
              v-model="v$.activa.$model"
              required
            />
            <div v-if="v$.activa.$anyDirty && v$.activa.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activa.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Fecha Cierre</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="historia-clinica-fechaCierre"
                  v-model="v$.fechaCierre.$model"
                  name="fechaCierre"
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
                id="historia-clinica-fechaCierre"
                data-cy="fechaCierre"
                type="text"
                class="form-control"
                name="fechaCierre"
                :class="{ valid: !v$.fechaCierre.$invalid, invalid: v$.fechaCierre.$invalid }"
                v-model="v$.fechaCierre.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Motivo Cierre</label>
            <input
              type="text"
              class="form-control"
              name="motivoCierre"
              id="historia-clinica-motivoCierre"
              data-cy="motivoCierre"
              :class="{ valid: !v$.motivoCierre.$invalid, invalid: v$.motivoCierre.$invalid }"
              v-model="v$.motivoCierre.$model"
            />
            <div v-if="v$.motivoCierre.$anyDirty && v$.motivoCierre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.motivoCierre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="historia-clinica">Paciente</label>
            <select
              class="form-control"
              id="historia-clinica-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="historiaClinica.paciente"
              required
            >
              <option v-if="!historiaClinica.paciente" :value="null" selected></option>
              <option
                :value="
                  historiaClinica.paciente && pacienteOption.id === historiaClinica.paciente.id ? historiaClinica.paciente : pacienteOption
                "
                v-for="pacienteOption in pacientes"
                :key="pacienteOption.id"
              >
                {{ pacienteOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.paciente.$anyDirty && v$.paciente.$invalid">
            <small class="form-text text-danger" v-for="error of v$.paciente.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./historia-clinica-update.component.ts"></script>
