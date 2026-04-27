<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.signosVitales.home.createOrEditLabel" data-cy="SignosVitalesCreateUpdateHeading">
          Crear o editar Signos Vitales
        </h2>
        <div>
          <div class="mb-3" v-if="signosVitales.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="signosVitales.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Fecha Hora Registro</label>
            <div class="d-flex">
              <input
                id="signos-vitales-fechaHoraRegistro"
                data-cy="fechaHoraRegistro"
                type="datetime-local"
                class="form-control"
                name="fechaHoraRegistro"
                :class="{ valid: !v$.fechaHoraRegistro.$invalid, invalid: v$.fechaHoraRegistro.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaHoraRegistro.$model)"
                @change="updateInstantField('fechaHoraRegistro', $event)"
              />
            </div>
            <div v-if="v$.fechaHoraRegistro.$anyDirty && v$.fechaHoraRegistro.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaHoraRegistro.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Peso</label>
            <input
              type="number"
              class="form-control"
              name="peso"
              id="signos-vitales-peso"
              data-cy="peso"
              :class="{ valid: !v$.peso.$invalid, invalid: v$.peso.$invalid }"
              v-model.number="v$.peso.$model"
            />
            <div v-if="v$.peso.$anyDirty && v$.peso.$invalid">
              <small class="form-text text-danger" v-for="error of v$.peso.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Talla</label>
            <input
              type="number"
              class="form-control"
              name="talla"
              id="signos-vitales-talla"
              data-cy="talla"
              :class="{ valid: !v$.talla.$invalid, invalid: v$.talla.$invalid }"
              v-model.number="v$.talla.$model"
            />
            <div v-if="v$.talla.$anyDirty && v$.talla.$invalid">
              <small class="form-text text-danger" v-for="error of v$.talla.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Temperatura</label>
            <input
              type="number"
              class="form-control"
              name="temperatura"
              id="signos-vitales-temperatura"
              data-cy="temperatura"
              :class="{ valid: !v$.temperatura.$invalid, invalid: v$.temperatura.$invalid }"
              v-model.number="v$.temperatura.$model"
            />
            <div v-if="v$.temperatura.$anyDirty && v$.temperatura.$invalid">
              <small class="form-text text-danger" v-for="error of v$.temperatura.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Presion Arterial</label>
            <input
              type="text"
              class="form-control"
              name="presionArterial"
              id="signos-vitales-presionArterial"
              data-cy="presionArterial"
              :class="{ valid: !v$.presionArterial.$invalid, invalid: v$.presionArterial.$invalid }"
              v-model="v$.presionArterial.$model"
            />
            <div v-if="v$.presionArterial.$anyDirty && v$.presionArterial.$invalid">
              <small class="form-text text-danger" v-for="error of v$.presionArterial.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Frecuencia Cardiaca</label>
            <input
              type="number"
              class="form-control"
              name="frecuenciaCardiaca"
              id="signos-vitales-frecuenciaCardiaca"
              data-cy="frecuenciaCardiaca"
              :class="{ valid: !v$.frecuenciaCardiaca.$invalid, invalid: v$.frecuenciaCardiaca.$invalid }"
              v-model.number="v$.frecuenciaCardiaca.$model"
            />
            <div v-if="v$.frecuenciaCardiaca.$anyDirty && v$.frecuenciaCardiaca.$invalid">
              <small class="form-text text-danger" v-for="error of v$.frecuenciaCardiaca.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Frecuencia Respiratoria</label>
            <input
              type="number"
              class="form-control"
              name="frecuenciaRespiratoria"
              id="signos-vitales-frecuenciaRespiratoria"
              data-cy="frecuenciaRespiratoria"
              :class="{ valid: !v$.frecuenciaRespiratoria.$invalid, invalid: v$.frecuenciaRespiratoria.$invalid }"
              v-model.number="v$.frecuenciaRespiratoria.$model"
            />
            <div v-if="v$.frecuenciaRespiratoria.$anyDirty && v$.frecuenciaRespiratoria.$invalid">
              <small class="form-text text-danger" v-for="error of v$.frecuenciaRespiratoria.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Saturacion Oxigeno</label>
            <input
              type="number"
              class="form-control"
              name="saturacionOxigeno"
              id="signos-vitales-saturacionOxigeno"
              data-cy="saturacionOxigeno"
              :class="{ valid: !v$.saturacionOxigeno.$invalid, invalid: v$.saturacionOxigeno.$invalid }"
              v-model.number="v$.saturacionOxigeno.$model"
            />
            <div v-if="v$.saturacionOxigeno.$anyDirty && v$.saturacionOxigeno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.saturacionOxigeno.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="signos-vitales-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="signos-vitales">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="signos-vitales-activo"
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
            <label class="form-control-label" for="signos-vitales">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="signos-vitales-fechaAlta"
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
                id="signos-vitales-fechaAlta"
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
            <label class="form-control-label" for="signos-vitales">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="signos-vitales-fechaBaja"
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
                id="signos-vitales-fechaBaja"
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
            <label class="form-control-label" for="signos-vitales">Consulta</label>
            <select
              class="form-control"
              id="signos-vitales-consulta"
              data-cy="consulta"
              name="consulta"
              v-model="signosVitales.consulta"
              required
            >
              <option v-if="!signosVitales.consulta" :value="null" selected></option>
              <option
                :value="signosVitales.consulta && consultaOption.id === signosVitales.consulta.id ? signosVitales.consulta : consultaOption"
                v-for="consultaOption in consultas"
                :key="consultaOption.id"
              >
                {{ consultaOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.consulta.$anyDirty && v$.consulta.$invalid">
            <small class="form-text text-danger" v-for="error of v$.consulta.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./signos-vitales-update.component.ts"></script>
