<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.consulta.home.createOrEditLabel" data-cy="ConsultaCreateUpdateHeading">Crear o editar Consulta</h2>
        <div>
          <div class="mb-3" v-if="consulta.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="consulta.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="consulta-codigo"
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
            <label class="form-control-label" for="consulta">Fecha Hora Inicio</label>
            <div class="d-flex">
              <input
                id="consulta-fechaHoraInicio"
                data-cy="fechaHoraInicio"
                type="datetime-local"
                class="form-control"
                name="fechaHoraInicio"
                :class="{ valid: !v$.fechaHoraInicio.$invalid, invalid: v$.fechaHoraInicio.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaHoraInicio.$model)"
                @change="updateInstantField('fechaHoraInicio', $event)"
              />
            </div>
            <div v-if="v$.fechaHoraInicio.$anyDirty && v$.fechaHoraInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaHoraInicio.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Fecha Hora Fin</label>
            <div class="d-flex">
              <input
                id="consulta-fechaHoraFin"
                data-cy="fechaHoraFin"
                type="datetime-local"
                class="form-control"
                name="fechaHoraFin"
                :class="{ valid: !v$.fechaHoraFin.$invalid, invalid: v$.fechaHoraFin.$invalid }"
                :value="convertDateTimeFromServer(v$.fechaHoraFin.$model)"
                @change="updateInstantField('fechaHoraFin', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Sintomas</label>
            <textarea
              class="form-control"
              name="sintomas"
              id="consulta-sintomas"
              data-cy="sintomas"
              :class="{ valid: !v$.sintomas.$invalid, invalid: v$.sintomas.$invalid }"
              v-model="v$.sintomas.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Motivo Consulta</label>
            <input
              type="text"
              class="form-control"
              name="motivoConsulta"
              id="consulta-motivoConsulta"
              data-cy="motivoConsulta"
              :class="{ valid: !v$.motivoConsulta.$invalid, invalid: v$.motivoConsulta.$invalid }"
              v-model="v$.motivoConsulta.$model"
              required
            />
            <div v-if="v$.motivoConsulta.$anyDirty && v$.motivoConsulta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.motivoConsulta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Examen Fisico</label>
            <textarea
              class="form-control"
              name="examenFisico"
              id="consulta-examenFisico"
              data-cy="examenFisico"
              :class="{ valid: !v$.examenFisico.$invalid, invalid: v$.examenFisico.$invalid }"
              v-model="v$.examenFisico.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="consulta-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Indicaciones</label>
            <textarea
              class="form-control"
              name="indicaciones"
              id="consulta-indicaciones"
              data-cy="indicaciones"
              :class="{ valid: !v$.indicaciones.$invalid, invalid: v$.indicaciones.$invalid }"
              v-model="v$.indicaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Activa</label>
            <input
              type="checkbox"
              class="form-check"
              name="activa"
              id="consulta-activa"
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
            <label class="form-control-label" for="consulta">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="consulta-fechaAlta"
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
                id="consulta-fechaAlta"
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
            <label class="form-control-label" for="consulta">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="consulta-fechaBaja"
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
                id="consulta-fechaBaja"
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
            <label class="form-control-label" for="consulta">Turno</label>
            <select class="form-control" id="consulta-turno" data-cy="turno" name="turno" v-model="consulta.turno">
              <option :value="null"></option>
              <option
                :value="consulta.turno && turnoOption.id === consulta.turno.id ? consulta.turno : turnoOption"
                v-for="turnoOption in turnos"
                :key="turnoOption.id"
              >
                {{ turnoOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="consulta">Paciente</label>
            <select class="form-control" id="consulta-paciente" data-cy="paciente" name="paciente" v-model="consulta.paciente" required>
              <option v-if="!consulta.paciente" :value="null" selected></option>
              <option
                :value="consulta.paciente && pacienteOption.id === consulta.paciente.id ? consulta.paciente : pacienteOption"
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
            <label class="form-control-label" for="consulta">Medico</label>
            <select class="form-control" id="consulta-medico" data-cy="medico" name="medico" v-model="consulta.medico" required>
              <option v-if="!consulta.medico" :value="null" selected></option>
              <option
                :value="consulta.medico && medicoOption.id === consulta.medico.id ? consulta.medico : medicoOption"
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
            <label class="form-control-label" for="consulta">Historia Clinica</label>
            <select
              class="form-control"
              id="consulta-historiaClinica"
              data-cy="historiaClinica"
              name="historiaClinica"
              v-model="consulta.historiaClinica"
              required
            >
              <option v-if="!consulta.historiaClinica" :value="null" selected></option>
              <option
                :value="
                  consulta.historiaClinica && historiaClinicaOption.id === consulta.historiaClinica.id
                    ? consulta.historiaClinica
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
<script lang="ts" src="./consulta-update.component.ts"></script>
