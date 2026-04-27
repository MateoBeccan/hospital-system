<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.tratamiento.home.createOrEditLabel" data-cy="TratamientoCreateUpdateHeading">Crear o editar Tratamiento</h2>
        <div>
          <div class="mb-3" v-if="tratamiento.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tratamiento.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="tratamiento-codigo"
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
            <label class="form-control-label" for="tratamiento">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="tratamiento-descripcion"
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
            <label class="form-control-label" for="tratamiento">Fecha Inicio</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="tratamiento-fechaInicio"
                  v-model="v$.fechaInicio.$model"
                  name="fechaInicio"
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
                id="tratamiento-fechaInicio"
                data-cy="fechaInicio"
                type="text"
                class="form-control"
                name="fechaInicio"
                :class="{ valid: !v$.fechaInicio.$invalid, invalid: v$.fechaInicio.$invalid }"
                v-model="v$.fechaInicio.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaInicio.$anyDirty && v$.fechaInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaInicio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Fecha Fin</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="tratamiento-fechaFin"
                  v-model="v$.fechaFin.$model"
                  name="fechaFin"
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
                id="tratamiento-fechaFin"
                data-cy="fechaFin"
                type="text"
                class="form-control"
                name="fechaFin"
                :class="{ valid: !v$.fechaFin.$invalid, invalid: v$.fechaFin.$invalid }"
                v-model="v$.fechaFin.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="tratamiento-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Fecha Proxima Revision</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="tratamiento-fechaProximaRevision"
                  v-model="v$.fechaProximaRevision.$model"
                  name="fechaProximaRevision"
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
                id="tratamiento-fechaProximaRevision"
                data-cy="fechaProximaRevision"
                type="text"
                class="form-control"
                name="fechaProximaRevision"
                :class="{ valid: !v$.fechaProximaRevision.$invalid, invalid: v$.fechaProximaRevision.$invalid }"
                v-model="v$.fechaProximaRevision.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="tratamiento-activo"
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
            <label class="form-control-label" for="tratamiento">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="tratamiento-fechaAlta"
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
                id="tratamiento-fechaAlta"
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
            <label class="form-control-label" for="tratamiento">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="tratamiento-fechaBaja"
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
                id="tratamiento-fechaBaja"
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
            <label class="form-control-label" for="tratamiento">Diagnostico</label>
            <select
              class="form-control"
              id="tratamiento-diagnostico"
              data-cy="diagnostico"
              name="diagnostico"
              v-model="tratamiento.diagnostico"
              required
            >
              <option v-if="!tratamiento.diagnostico" :value="null" selected></option>
              <option
                :value="
                  tratamiento.diagnostico && diagnosticoOption.id === tratamiento.diagnostico.id
                    ? tratamiento.diagnostico
                    : diagnosticoOption
                "
                v-for="diagnosticoOption in diagnosticos"
                :key="diagnosticoOption.id"
              >
                {{ diagnosticoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.diagnostico.$anyDirty && v$.diagnostico.$invalid">
            <small class="form-text text-danger" v-for="error of v$.diagnostico.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="tratamiento">Estado Tratamiento</label>
            <select
              class="form-control"
              id="tratamiento-estadoTratamiento"
              data-cy="estadoTratamiento"
              name="estadoTratamiento"
              v-model="tratamiento.estadoTratamiento"
              required
            >
              <option v-if="!tratamiento.estadoTratamiento" :value="null" selected></option>
              <option
                :value="
                  tratamiento.estadoTratamiento && estadoTratamientoOption.id === tratamiento.estadoTratamiento.id
                    ? tratamiento.estadoTratamiento
                    : estadoTratamientoOption
                "
                v-for="estadoTratamientoOption in estadoTratamientos"
                :key="estadoTratamientoOption.id"
              >
                {{ estadoTratamientoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.estadoTratamiento.$anyDirty && v$.estadoTratamiento.$invalid">
            <small class="form-text text-danger" v-for="error of v$.estadoTratamiento.$errors" :key="error.$uid">{{
              error.$message
            }}</small>
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
<script lang="ts" src="./tratamiento-update.component.ts"></script>
