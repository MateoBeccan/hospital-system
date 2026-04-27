<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.diagnostico.home.createOrEditLabel" data-cy="DiagnosticoCreateUpdateHeading">Crear o editar Diagnostico</h2>
        <div>
          <div class="mb-3" v-if="diagnostico.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="diagnostico.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="diagnostico-codigo"
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
            <label class="form-control-label" for="diagnostico">Fecha Diagnostico</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="diagnostico-fechaDiagnostico"
                  v-model="v$.fechaDiagnostico.$model"
                  name="fechaDiagnostico"
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
                id="diagnostico-fechaDiagnostico"
                data-cy="fechaDiagnostico"
                type="text"
                class="form-control"
                name="fechaDiagnostico"
                :class="{ valid: !v$.fechaDiagnostico.$invalid, invalid: v$.fechaDiagnostico.$invalid }"
                v-model="v$.fechaDiagnostico.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaDiagnostico.$anyDirty && v$.fechaDiagnostico.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaDiagnostico.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="diagnostico-descripcion"
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
            <label class="form-control-label" for="diagnostico">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="diagnostico-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="diagnostico-activo"
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
            <label class="form-control-label" for="diagnostico">Fecha Resolucion</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="diagnostico-fechaResolucion"
                  v-model="v$.fechaResolucion.$model"
                  name="fechaResolucion"
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
                id="diagnostico-fechaResolucion"
                data-cy="fechaResolucion"
                type="text"
                class="form-control"
                name="fechaResolucion"
                :class="{ valid: !v$.fechaResolucion.$invalid, invalid: v$.fechaResolucion.$invalid }"
                v-model="v$.fechaResolucion.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Es Principal</label>
            <input
              type="checkbox"
              class="form-check"
              name="esPrincipal"
              id="diagnostico-esPrincipal"
              data-cy="esPrincipal"
              :class="{ valid: !v$.esPrincipal.$invalid, invalid: v$.esPrincipal.$invalid }"
              v-model="v$.esPrincipal.$model"
              required
            />
            <div v-if="v$.esPrincipal.$anyDirty && v$.esPrincipal.$invalid">
              <small class="form-text text-danger" v-for="error of v$.esPrincipal.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="diagnostico-fechaAlta"
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
                id="diagnostico-fechaAlta"
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
            <label class="form-control-label" for="diagnostico">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="diagnostico-fechaBaja"
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
                id="diagnostico-fechaBaja"
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
            <label class="form-control-label" for="diagnostico">Consulta</label>
            <select
              class="form-control"
              id="diagnostico-consulta"
              data-cy="consulta"
              name="consulta"
              v-model="diagnostico.consulta"
              required
            >
              <option v-if="!diagnostico.consulta" :value="null" selected></option>
              <option
                :value="diagnostico.consulta && consultaOption.id === diagnostico.consulta.id ? diagnostico.consulta : consultaOption"
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
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Paciente</label>
            <select
              class="form-control"
              id="diagnostico-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="diagnostico.paciente"
              required
            >
              <option v-if="!diagnostico.paciente" :value="null" selected></option>
              <option
                :value="diagnostico.paciente && pacienteOption.id === diagnostico.paciente.id ? diagnostico.paciente : pacienteOption"
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
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Medico</label>
            <select class="form-control" id="diagnostico-medico" data-cy="medico" name="medico" v-model="diagnostico.medico" required>
              <option v-if="!diagnostico.medico" :value="null" selected></option>
              <option
                :value="diagnostico.medico && medicoOption.id === diagnostico.medico.id ? diagnostico.medico : medicoOption"
                v-for="medicoOption in medicos"
                :key="medicoOption.id"
              >
                {{ medicoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.medico.$anyDirty && v$.medico.$invalid">
            <small class="form-text text-danger" v-for="error of v$.medico.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Tipo Diagnostico</label>
            <select
              class="form-control"
              id="diagnostico-tipoDiagnostico"
              data-cy="tipoDiagnostico"
              name="tipoDiagnostico"
              v-model="diagnostico.tipoDiagnostico"
              required
            >
              <option v-if="!diagnostico.tipoDiagnostico" :value="null" selected></option>
              <option
                :value="
                  diagnostico.tipoDiagnostico && tipoDiagnosticoOption.id === diagnostico.tipoDiagnostico.id
                    ? diagnostico.tipoDiagnostico
                    : tipoDiagnosticoOption
                "
                v-for="tipoDiagnosticoOption in tipoDiagnosticos"
                :key="tipoDiagnosticoOption.id"
              >
                {{ tipoDiagnosticoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.tipoDiagnostico.$anyDirty && v$.tipoDiagnostico.$invalid">
            <small class="form-text text-danger" v-for="error of v$.tipoDiagnostico.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="diagnostico">Estado Diagnostico</label>
            <select
              class="form-control"
              id="diagnostico-estadoDiagnostico"
              data-cy="estadoDiagnostico"
              name="estadoDiagnostico"
              v-model="diagnostico.estadoDiagnostico"
              required
            >
              <option v-if="!diagnostico.estadoDiagnostico" :value="null" selected></option>
              <option
                :value="
                  diagnostico.estadoDiagnostico && estadoDiagnosticoOption.id === diagnostico.estadoDiagnostico.id
                    ? diagnostico.estadoDiagnostico
                    : estadoDiagnosticoOption
                "
                v-for="estadoDiagnosticoOption in estadoDiagnosticos"
                :key="estadoDiagnosticoOption.id"
              >
                {{ estadoDiagnosticoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.estadoDiagnostico.$anyDirty && v$.estadoDiagnostico.$invalid">
            <small class="form-text text-danger" v-for="error of v$.estadoDiagnostico.$errors" :key="error.$uid">{{
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
<script lang="ts" src="./diagnostico-update.component.ts"></script>
