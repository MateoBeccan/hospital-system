<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.empleado.home.createOrEditLabel" data-cy="EmpleadoCreateUpdateHeading">Crear o editar Empleado</h2>
        <div>
          <div class="mb-3" v-if="empleado.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="empleado.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="empleado">Legajo</label>
            <input
              type="text"
              class="form-control"
              name="legajo"
              id="empleado-legajo"
              data-cy="legajo"
              :class="{ valid: !v$.legajo.$invalid, invalid: v$.legajo.$invalid }"
              v-model="v$.legajo.$model"
              required
            />
            <div v-if="v$.legajo.$anyDirty && v$.legajo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.legajo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="empleado">Fecha Ingreso</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="empleado-fechaIngreso"
                  v-model="v$.fechaIngreso.$model"
                  name="fechaIngreso"
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
                id="empleado-fechaIngreso"
                data-cy="fechaIngreso"
                type="text"
                class="form-control"
                name="fechaIngreso"
                :class="{ valid: !v$.fechaIngreso.$invalid, invalid: v$.fechaIngreso.$invalid }"
                v-model="v$.fechaIngreso.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaIngreso.$anyDirty && v$.fechaIngreso.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaIngreso.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="empleado">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="empleado-fechaBaja"
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
                id="empleado-fechaBaja"
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
            <label class="form-control-label" for="empleado">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="empleado-activo"
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
            <label class="form-control-label" for="empleado">Persona</label>
            <select class="form-control" id="empleado-persona" data-cy="persona" name="persona" v-model="empleado.persona" required>
              <option v-if="!empleado.persona" :value="null" selected></option>
              <option
                :value="empleado.persona && personaOption.id === empleado.persona.id ? empleado.persona : personaOption"
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
            <label class="form-control-label" for="empleado">Tipo Empleado</label>
            <select
              class="form-control"
              id="empleado-tipoEmpleado"
              data-cy="tipoEmpleado"
              name="tipoEmpleado"
              v-model="empleado.tipoEmpleado"
              required
            >
              <option v-if="!empleado.tipoEmpleado" :value="null" selected></option>
              <option
                :value="
                  empleado.tipoEmpleado && tipoEmpleadoOption.id === empleado.tipoEmpleado.id ? empleado.tipoEmpleado : tipoEmpleadoOption
                "
                v-for="tipoEmpleadoOption in tipoEmpleados"
                :key="tipoEmpleadoOption.id"
              >
                {{ tipoEmpleadoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.tipoEmpleado.$anyDirty && v$.tipoEmpleado.$invalid">
            <small class="form-text text-danger" v-for="error of v$.tipoEmpleado.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="empleado">Estado Laboral</label>
            <select
              class="form-control"
              id="empleado-estadoLaboral"
              data-cy="estadoLaboral"
              name="estadoLaboral"
              v-model="empleado.estadoLaboral"
              required
            >
              <option v-if="!empleado.estadoLaboral" :value="null" selected></option>
              <option
                :value="
                  empleado.estadoLaboral && estadoLaboralOption.id === empleado.estadoLaboral.id
                    ? empleado.estadoLaboral
                    : estadoLaboralOption
                "
                v-for="estadoLaboralOption in estadoLaborals"
                :key="estadoLaboralOption.id"
              >
                {{ estadoLaboralOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.estadoLaboral.$anyDirty && v$.estadoLaboral.$invalid">
            <small class="form-text text-danger" v-for="error of v$.estadoLaboral.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="empleado">Cargo</label>
            <select class="form-control" id="empleado-cargo" data-cy="cargo" name="cargo" v-model="empleado.cargo" required>
              <option v-if="!empleado.cargo" :value="null" selected></option>
              <option
                :value="empleado.cargo && cargoOption.id === empleado.cargo.id ? empleado.cargo : cargoOption"
                v-for="cargoOption in cargos"
                :key="cargoOption.id"
              >
                {{ cargoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.cargo.$anyDirty && v$.cargo.$invalid">
            <small class="form-text text-danger" v-for="error of v$.cargo.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./empleado-update.component.ts"></script>
