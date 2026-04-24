<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.antecedenteClinico.home.createOrEditLabel" data-cy="AntecedenteClinicoCreateUpdateHeading">
          Crear o editar Antecedente Clinico
        </h2>
        <div>
          <div class="mb-3" v-if="antecedenteClinico.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="antecedenteClinico.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="antecedente-clinico">Titulo</label>
            <input
              type="text"
              class="form-control"
              name="titulo"
              id="antecedente-clinico-titulo"
              data-cy="titulo"
              :class="{ valid: !v$.titulo.$invalid, invalid: v$.titulo.$invalid }"
              v-model="v$.titulo.$model"
              required
            />
            <div v-if="v$.titulo.$anyDirty && v$.titulo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.titulo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="antecedente-clinico">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="antecedente-clinico-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
              required
            />
            <div v-if="v$.descripcion.$anyDirty && v$.descripcion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.descripcion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="antecedente-clinico">Fecha Registro</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="antecedente-clinico-fechaRegistro"
                  v-model="v$.fechaRegistro.$model"
                  name="fechaRegistro"
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
                id="antecedente-clinico-fechaRegistro"
                data-cy="fechaRegistro"
                type="text"
                class="form-control"
                name="fechaRegistro"
                :class="{ valid: !v$.fechaRegistro.$invalid, invalid: v$.fechaRegistro.$invalid }"
                v-model="v$.fechaRegistro.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaRegistro.$anyDirty && v$.fechaRegistro.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaRegistro.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="antecedente-clinico">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="antecedente-clinico-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="antecedente-clinico">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="antecedente-clinico-activo"
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
            <label class="form-control-label" for="antecedente-clinico">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="antecedente-clinico-fechaAlta"
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
                id="antecedente-clinico-fechaAlta"
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
            <label class="form-control-label" for="antecedente-clinico">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="antecedente-clinico-fechaBaja"
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
                id="antecedente-clinico-fechaBaja"
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
            <label class="form-control-label" for="antecedente-clinico">Historia Clinica</label>
            <select
              class="form-control"
              id="antecedente-clinico-historiaClinica"
              data-cy="historiaClinica"
              name="historiaClinica"
              v-model="antecedenteClinico.historiaClinica"
              required
            >
              <option v-if="!antecedenteClinico.historiaClinica" :value="null" selected></option>
              <option
                :value="
                  antecedenteClinico.historiaClinica && historiaClinicaOption.id === antecedenteClinico.historiaClinica.id
                    ? antecedenteClinico.historiaClinica
                    : historiaClinicaOption
                "
                v-for="historiaClinicaOption in historiaClinicas"
                :key="historiaClinicaOption.id"
              >
                {{ historiaClinicaOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.historiaClinica.$anyDirty && v$.historiaClinica.$invalid">
            <small class="form-text text-danger" v-for="error of v$.historiaClinica.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./antecedente-clinico-update.component.ts"></script>
