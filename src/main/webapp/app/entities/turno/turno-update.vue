<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.turno.home.createOrEditLabel" data-cy="TurnoCreateUpdateHeading">Crear o editar Turno</h2>
        <div>
          <div class="mb-3" v-if="turno.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="turno.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="turno-codigo"
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
            <label class="form-control-label" for="turno">Fecha Hora</label>
            <div class="d-flex">
              <input
                id="turno-fechaHora"
                data-cy="fechaHora"
                type="datetime-local"
                class="form-control"
                name="fechaHora"
                :class="{ valid: !v$.fechaHora.$invalid, invalid: v$.fechaHora.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaHora.$model)"
                @change="updateInstantField('fechaHora', $event)"
              />
            </div>
            <div v-if="v$.fechaHora.$anyDirty && v$.fechaHora.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaHora.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Duracion Minutos</label>
            <input
              type="number"
              class="form-control"
              name="duracionMinutos"
              id="turno-duracionMinutos"
              data-cy="duracionMinutos"
              :class="{ valid: !v$.duracionMinutos.$invalid, invalid: v$.duracionMinutos.$invalid }"
              v-model.number="v$.duracionMinutos.$model"
              required
            />
            <div v-if="v$.duracionMinutos.$anyDirty && v$.duracionMinutos.$invalid">
              <small class="form-text text-danger" v-for="error of v$.duracionMinutos.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Motivo Consulta</label>
            <input
              type="text"
              class="form-control"
              name="motivoConsulta"
              id="turno-motivoConsulta"
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
            <label class="form-control-label" for="turno">Observaciones</label>
            <textarea
              class="form-control"
              name="observaciones"
              id="turno-observaciones"
              data-cy="observaciones"
              :class="{ valid: !v$.observaciones.$invalid, invalid: v$.observaciones.$invalid }"
              v-model="v$.observaciones.$model"
            ></textarea>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Fecha Creacion</label>
            <div class="d-flex">
              <input
                id="turno-fechaCreacion"
                data-cy="fechaCreacion"
                type="datetime-local"
                class="form-control"
                name="fechaCreacion"
                :class="{ valid: !v$.fechaCreacion.$invalid, invalid: v$.fechaCreacion.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaCreacion.$model)"
                @change="updateInstantField('fechaCreacion', $event)"
              />
            </div>
            <div v-if="v$.fechaCreacion.$anyDirty && v$.fechaCreacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaCreacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="turno-activo"
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
            <label class="form-control-label" for="turno">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="turno-fechaAlta"
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
                id="turno-fechaAlta"
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
            <label class="form-control-label" for="turno">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="turno-fechaBaja"
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
                id="turno-fechaBaja"
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
            <label class="form-control-label" for="turno">Paciente</label>
            <select class="form-control" id="turno-paciente" data-cy="paciente" name="paciente" v-model="turno.paciente" required>
              <option v-if="!turno.paciente" :value="null" selected></option>
              <option
                :value="turno.paciente && pacienteOption.id === turno.paciente.id ? turno.paciente : pacienteOption"
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
            <label class="form-control-label" for="turno">Medico</label>
            <select class="form-control" id="turno-medico" data-cy="medico" name="medico" v-model="turno.medico" required>
              <option v-if="!turno.medico" :value="null" selected></option>
              <option
                :value="turno.medico && medicoOption.id === turno.medico.id ? turno.medico : medicoOption"
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
            <label class="form-control-label" for="turno">Especialidad</label>
            <select
              class="form-control"
              id="turno-especialidad"
              data-cy="especialidad"
              name="especialidad"
              v-model="turno.especialidad"
              required
            >
              <option v-if="!turno.especialidad" :value="null" selected></option>
              <option
                :value="turno.especialidad && especialidadOption.id === turno.especialidad.id ? turno.especialidad : especialidadOption"
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
          <div class="mb-3">
            <label class="form-control-label" for="turno">Estado Turno</label>
            <select
              class="form-control"
              id="turno-estadoTurno"
              data-cy="estadoTurno"
              name="estadoTurno"
              v-model="turno.estadoTurno"
              required
            >
              <option v-if="!turno.estadoTurno" :value="null" selected></option>
              <option
                :value="turno.estadoTurno && estadoTurnoOption.id === turno.estadoTurno.id ? turno.estadoTurno : estadoTurnoOption"
                v-for="estadoTurnoOption in estadoTurnos"
                :key="estadoTurnoOption.id"
              >
                {{ estadoTurnoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.estadoTurno.$anyDirty && v$.estadoTurno.$invalid">
            <small class="form-text text-danger" v-for="error of v$.estadoTurno.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="turno">Canal Solicitud</label>
            <select
              class="form-control"
              id="turno-canalSolicitud"
              data-cy="canalSolicitud"
              name="canalSolicitud"
              v-model="turno.canalSolicitud"
              required
            >
              <option v-if="!turno.canalSolicitud" :value="null" selected></option>
              <option
                :value="
                  turno.canalSolicitud && canalSolicitudOption.id === turno.canalSolicitud.id ? turno.canalSolicitud : canalSolicitudOption
                "
                v-for="canalSolicitudOption in canalSolicituds"
                :key="canalSolicitudOption.id"
              >
                {{ canalSolicitudOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.canalSolicitud.$anyDirty && v$.canalSolicitud.$invalid">
            <small class="form-text text-danger" v-for="error of v$.canalSolicitud.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./turno-update.component.ts"></script>
