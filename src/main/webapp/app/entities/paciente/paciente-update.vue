<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.paciente.home.createOrEditLabel" data-cy="PacienteCreateUpdateHeading">Crear o editar Paciente</h2>
        <div>
          <div class="mb-3" v-if="paciente.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="paciente.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Numero Historia Clinica</label>
            <input
              type="text"
              class="form-control"
              name="numeroHistoriaClinica"
              id="paciente-numeroHistoriaClinica"
              data-cy="numeroHistoriaClinica"
              :class="{ valid: !v$.numeroHistoriaClinica.$invalid, invalid: v$.numeroHistoriaClinica.$invalid }"
              v-model="v$.numeroHistoriaClinica.$model"
              required
            />
            <div v-if="v$.numeroHistoriaClinica.$anyDirty && v$.numeroHistoriaClinica.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroHistoriaClinica.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Alergias Generales</label>
            <textarea
              class="form-control"
              name="alergiasGenerales"
              id="paciente-alergiasGenerales"
              data-cy="alergiasGenerales"
              :class="{ valid: !v$.alergiasGenerales.$invalid, invalid: v$.alergiasGenerales.$invalid }"
              v-model="v$.alergiasGenerales.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="paciente-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-fechaAlta"
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
                id="paciente-fechaAlta"
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
            <label class="form-control-label" for="paciente">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-fechaBaja"
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
                id="paciente-fechaBaja"
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
            <label class="form-control-label" for="paciente">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="paciente-activo"
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
            <label class="form-control-label" for="paciente">Persona</label>
            <select class="form-control" id="paciente-persona" data-cy="persona" name="persona" v-model="paciente.persona" required>
              <option v-if="!paciente.persona" :value="null" selected></option>
              <option
                :value="paciente.persona && personaOption.id === paciente.persona.id ? paciente.persona : personaOption"
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
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Obra Social</label>
            <select class="form-control" id="paciente-obraSocial" data-cy="obraSocial" name="obraSocial" v-model="paciente.obraSocial">
              <option :value="null"></option>
              <option
                :value="paciente.obraSocial && obraSocialOption.id === paciente.obraSocial.id ? paciente.obraSocial : obraSocialOption"
                v-for="obraSocialOption in obraSocials"
                :key="obraSocialOption.id"
              >
                {{ obraSocialOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Grupo Sanguineo</label>
            <select
              class="form-control"
              id="paciente-grupoSanguineo"
              data-cy="grupoSanguineo"
              name="grupoSanguineo"
              v-model="paciente.grupoSanguineo"
            >
              <option :value="null"></option>
              <option
                :value="
                  paciente.grupoSanguineo && grupoSanguineoOption.id === paciente.grupoSanguineo.id
                    ? paciente.grupoSanguineo
                    : grupoSanguineoOption
                "
                v-for="grupoSanguineoOption in grupoSanguineos"
                :key="grupoSanguineoOption.id"
              >
                {{ grupoSanguineoOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="paciente">Factor Rh</label>
            <select class="form-control" id="paciente-factorRh" data-cy="factorRh" name="factorRh" v-model="paciente.factorRh">
              <option :value="null"></option>
              <option
                :value="paciente.factorRh && factorRhOption.id === paciente.factorRh.id ? paciente.factorRh : factorRhOption"
                v-for="factorRhOption in factorRhs"
                :key="factorRhOption.id"
              >
                {{ factorRhOption.id }}
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
<script lang="ts" src="./paciente-update.component.ts"></script>
